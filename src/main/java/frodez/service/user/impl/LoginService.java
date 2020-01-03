package frodez.service.user.impl;

import frodez.config.aop.exception.annotation.Error;
import frodez.config.security.util.AuthorityUtil;
import frodez.config.security.util.TokenUtil;
import frodez.constant.errors.code.ErrorCode;
import frodez.dao.model.result.login.RefreshInfo;
import frodez.dao.model.result.user.UserBaseInfo;
import frodez.dao.model.result.user.UserEndpointDetail;
import frodez.dao.param.login.LoginUser;
import frodez.dao.param.login.RefreshToken;
import frodez.service.cache.facade.user.IdTokenCache;
import frodez.service.cache.facade.user.RoleCache;
import frodez.service.cache.facade.user.UserCache;
import frodez.service.user.facade.ILoginService;
import frodez.service.user.facade.IUserManageService;
import frodez.util.beans.result.Result;
import frodez.util.reflect.BeanUtil;
import frodez.util.spring.MVCUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
@Error(ErrorCode.LOGIN_SERVICE_ERROR)
public class LoginService implements ILoginService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityContextLogoutHandler logoutHandler;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("idTokenRedisCache")
	private IdTokenCache idTokenCache;

	@Autowired
	@Qualifier("userMapCache")
	private UserCache userCache;

	@Autowired
	@Qualifier("roleMapCache")
	private RoleCache roleCache;

	@Autowired
	private IUserManageService userManageService;

	@Override
	public Result login(LoginUser param) {
		Result result = userManageService.getEndpointPermission(param.getUsername());
		if (result.unable()) {
			return result;
		}
		UserEndpointDetail permission = result.as(UserEndpointDetail.class);
		if (!passwordEncoder.matches(param.getPassword(), permission.getUser().getPassword())) {
			return Result.fail("用户名或密码错误");
		}
		Long userId = permission.getUser().getId();
		//realToken
		String token = TokenUtil.generate(permission);
		//保存id-token到缓存,退出时需要删除token,重新登录时需要重新保存token
		String oldToken = idTokenCache.getToken(userId);
		if (oldToken != null) {
			idTokenCache.remove(oldToken);
		}
		idTokenCache.save(userId, token);
		//保存id-UserBaseInfo,id-Role到缓存
		userCache.save(userId, BeanUtil.copy(permission.getUser(), UserBaseInfo::new));
		roleCache.save(userId, permission.getRole());
		Authentication authentication = new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword());
		authentication = authenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return Result.success(token);
	}

	@Override
	public Result refresh(RefreshToken param) {
		UserDetails userDetails;
		//判断token是否能通过验证(必须考虑过期)
		try {
			userDetails = TokenUtil.verifyWithNoExpired(param.getOldToken());
		} catch (Exception e) {
			return Result.noAuth();
		}
		//判断验证通过的token中姓名与填写的姓名是否一致
		if (!param.getUsername().equals(userDetails.getUsername())) {
			return Result.fail("当前用户和重新登录的用户不符");
		}

		//判断token对应账号id和userName对应账号id是否一致,还有账号是否正常
		Result result = userManageService.getEndpointPermission(param.getUsername());
		if (result.unable()) {
			return result;
		}
		UserEndpointDetail permission = result.as(UserEndpointDetail.class);
		if (!param.getUsername().equals(permission.getUser().getName())) {
			return Result.fail("账户信息验证失败");
		}
		//判断该token的用户是否已登录或者已异地登录
		Long userId = idTokenCache.getId(param.getOldToken());
		if (userId == null) {
			if (idTokenCache.exist(permission.getUser().getId())) {
				return Result.fail("该账户已于异地登录");
			}
			return Result.notLogin();
		}
		//判断结束
		//登出
		logoutHandler.logout(MVCUtil.request(), MVCUtil.response(), SecurityContextHolder.getContext().getAuthentication());
		//登入
		List<GrantedAuthority> authorities = AuthorityUtil.make(permission.getEndpoints());
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(MVCUtil.request()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//用刚查询出来的用户权限信息生成新token,这样可以做到重新登录时更新权限
		String newToken = TokenUtil.generate(permission);
		//覆盖cache,直接覆盖老token
		String oldToken = idTokenCache.getToken(userId);
		if (oldToken != null) {
			idTokenCache.remove(oldToken);
		}
		idTokenCache.save(userId, newToken);
		//保存id-UserBaseInfo,id-Role到缓存
		userCache.save(userId, BeanUtil.copy(permission.getUser(), UserBaseInfo::new));
		roleCache.save(userId, permission.getRole());
		RefreshInfo data = new RefreshInfo();
		data.setNewToken(newToken);
		data.setRedirect(param.getRedirect());
		return Result.success(data);
	}

	@Override
	public Result logout() {
		HttpServletRequest request = MVCUtil.request();
		String token = TokenUtil.getRealToken(request);
		if (!idTokenCache.exist(token)) {
			return Result.fail("用户已下线");
		}
		logoutHandler.logout(request, MVCUtil.response(), SecurityContextHolder.getContext().getAuthentication());
		//删除缓存中的token,但不删除缓存中的User和Role
		Long userId = idTokenCache.getId(token);
		idTokenCache.remove(userId);
		idTokenCache.remove(token);
		return Result.success();
	}

}
