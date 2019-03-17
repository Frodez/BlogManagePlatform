package frodez.config.security.user;

import frodez.config.security.util.AuthorityUtil;
import frodez.dao.result.user.UserInfo;
import frodez.service.user.facade.IAuthorityService;
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
	 * 用户授权服务
	 */
	@Autowired
	private IAuthorityService authorityService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Result result = authorityService.getUserInfo(username);
		if (result.unable()) {
			throw new UsernameNotFoundException(result.getMessage());
		}
		UserInfo userInfo = result.as(UserInfo.class);
		return new User(userInfo.getName(), userInfo.getPassword(), AuthorityUtil.make(userInfo
			.getPermissionList()));
	}

}
