package frodez.config.security.user;

import frodez.config.security.util.AuthorityUtil;
import frodez.dao.model.result.user.UserEndpointDetail;
import frodez.service.user.facade.IUserManageService;
import frodez.util.beans.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 验证信息获取服务
 * @author Frodez
 * @date 2018-11-14
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	/**
	 * 用户管理服务
	 */
	@Autowired
	private IUserManageService userManageService;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Result result = userManageService.getEndpointPermission(name).orThrowMessage(UsernameNotFoundException::new);
		UserEndpointDetail info = result.as(UserEndpointDetail.class);
		//这里必须设置权限
		//详情见frodez.config.security.filter.TokenFilter.doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		//和frodez.config.security.auth.AuthorityManager.decide(Authentication auth, Object object, Collection<ConfigAttribute> permissions)方法
		return new User(name, info.getUser().getPassword(), AuthorityUtil.make(info.getEndpoints()));
	}

}
