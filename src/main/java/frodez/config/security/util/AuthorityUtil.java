package frodez.config.security.util;

import frodez.dao.result.user.PermissionInfo;
import frodez.util.common.StreamUtil;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 权限信息工具类
 * @author Frodez
 * @date 2018-11-14
 */
public class AuthorityUtil {

	/**
	 * 生成权限信息
	 * @author Frodez
	 * @param authorities 权限信息
	 * @date 2018-11-21
	 */
	public static List<GrantedAuthority> make(List<PermissionInfo> authorities) {
		return StreamUtil.list(authorities, authority -> new SimpleGrantedAuthority(authority.getName()));
	}

	/**
	 * 生成权限信息
	 * @author Frodez
	 * @param authorities 权限信息
	 * @date 2018-11-21
	 */
	public static List<GrantedAuthority> make(String... authorities) {
		return StreamUtil.list(authorities, SimpleGrantedAuthority::new);
	}

	/**
	 * 获取权限信息
	 * @author Frodez
	 * @param UserDetails 用户信息
	 * @date 2018-11-21
	 */
	public static String[] get(UserDetails user) {
		return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
	}

}
