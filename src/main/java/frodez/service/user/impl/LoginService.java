package frodez.service.user.impl;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.security.util.TokenUtil;
import frodez.dao.param.user.DoLogin;
import frodez.dao.param.user.DoRefresh;
import frodez.dao.result.user.PermissionInfo;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.vm.facade.TokenCache;
import frodez.service.user.facade.IAuthorityService;
import frodez.service.user.facade.ILoginService;
import frodez.util.beans.result.Result;
import frodez.util.spring.MVCUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

/**
 * 登录管理服务
 * @author Frodez
 * @date 2018-11-14
 */
@Slf4j
@Service
public class LoginService implements ILoginService {

	/**
	 * spring security验证管理器
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityContextLogoutHandler logoutHandler;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenCache tokenCache;

	@Autowired
	private IAuthorityService authorityService;

	@Check
	@Override
	public Result login(DoLogin param) {
		try {
			Result result = authorityService.getUserInfo(param.getUsername());
			if (result.unable()) {
				return result;
			}
			UserInfo userInfo = result.as(UserInfo.class);
			if (!passwordEncoder.matches(param.getPassword(), userInfo.getPassword())) {
				return Result.fail("用户名或密码错误!");
			}
			if (tokenCache.existValue(userInfo)) {
				return Result.fail("用户已登录!");
			}
			List<String> authorities = userInfo.getPermissionList().stream().map(PermissionInfo::getName).collect(
				Collectors.toList());
			//realToken
			String token = TokenUtil.generate(param.getUsername(), authorities);
			tokenCache.save(token, userInfo);
			SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword())));
			return Result.success(token);
		} catch (Exception e) {
			log.error("[login]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result refresh(DoRefresh param) {
		try {
			Result result = authorityService.getUserInfo(param.getUsername());
			if (result.unable()) {
				return result;
			}
			UserInfo userInfo = result.as(UserInfo.class);
			UserDetails userDetails = TokenUtil.verifyWithNoExpired(param.getOldToken());
			//这里的userDetails.password已经加密了
			if (!userDetails.getPassword().equals(userInfo.getPassword())) {
				return Result.fail("用户名或密码错误!");
			}
			List<String> authorities = userInfo.getPermissionList().stream().map(PermissionInfo::getName).collect(
				Collectors.toList());
			//realToken
			String token = TokenUtil.generate(param.getUsername(), authorities);
			tokenCache.remove(param.getOldToken());
			tokenCache.save(token, userInfo);
			logoutHandler.logout(MVCUtil.request(), MVCUtil.response(), SecurityContextHolder.getContext()
				.getAuthentication());
			SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(param.getUsername(), userInfo.getPassword())));
			return Result.success(token);
		} catch (Exception e) {
			log.error("[reLogin]", e);
			return Result.errorService();
		}
	}

	@Override
	public Result logout() {
		try {
			HttpServletRequest request = MVCUtil.request();
			String token = TokenUtil.getRealToken(request);
			if (!tokenCache.existKey(token)) {
				return Result.fail("用户已下线!");
			}
			tokenCache.remove(token);
			logoutHandler.logout(request, MVCUtil.response(), SecurityContextHolder.getContext().getAuthentication());
			return Result.success();
		} catch (Exception e) {
			log.error("[logout]", e);
			return Result.errorService();
		}
	}

}
