package info.frodez.config.security.realization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import info.frodez.dao.result.user.UserInfo;
import info.frodez.service.IUserAuthorityService;
import info.frodez.util.result.Result;

/**
 * 验证信息获取服务
 * @author Frodez
 * @date 2018-11-14
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private IUserAuthorityService userAuthorityService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Result result = userAuthorityService.getUserInfoByName(username);
		if(!result.isSuccess()) {
			throw new UsernameNotFoundException(result.getMessage());
		}
		UserInfo userInfo = result.getData(UserInfo.class);
		return new User(userInfo.getName(), userInfo.getPassword(), AuthorityUtil.createGrantedAuthorities(userInfo.getPermissionList()));
	}

}
