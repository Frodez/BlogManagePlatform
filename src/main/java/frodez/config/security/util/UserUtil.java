package frodez.config.security.util;

import frodez.dao.model.result.user.UserBaseInfo;
import frodez.dao.model.result.user.UserInfo;
import frodez.dao.model.table.user.Role;
import frodez.service.cache.facade.config.SettingCache;
import frodez.service.cache.facade.user.IdTokenCache;
import frodez.service.cache.facade.user.RoleCache;
import frodez.service.cache.facade.user.UserCache;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.MVCUtil;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 当前用户工具类
 * @author Frodez
 * @date 2019-12-29
 */
@Slf4j
@Component
@DependsOn("contextUtil")
public class UserUtil {

	private static IdTokenCache idTokenCache;

	private static UserCache userCache;

	private static RoleCache roleCache;

	private static SettingCache settingCache;

	@PostConstruct
	private void init() {
		idTokenCache = ContextUtil.bean("idTokenRedisCache", IdTokenCache.class);
		userCache = ContextUtil.bean("userMapCache", UserCache.class);
		roleCache = ContextUtil.bean("roleMapCache", RoleCache.class);
		settingCache = ContextUtil.bean("settingMapCache", SettingCache.class);
		Assert.notNull(idTokenCache, "idTokenCache must not be null");
		Assert.notNull(userCache, "userCache must not be null");
		Assert.notNull(settingCache, "settingCache must not be null");
	}

	/**
	 * 获取当前用户token,不能用于免验证URI中
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static String token() {
		HttpServletRequest request = MVCUtil.request();
		if (!Matcher.needVerify(request)) {
			log.warn("[UserUtil.get]不能在免验证URI中获取token信息!");
			return null;
		}
		return TokenUtil.getRealToken(request);
	}

	/**
	 * 获取当前访问者的ID,不能用于免验证URI中,会抛出异常
	 * @author Frodez
	 * @date 2019-12-29
	 */
	@SneakyThrows
	public static Long id() {
		String token = token();
		if (token == null) {
			return null;
		}
		return idTokenCache.getId(token);
	}

	/**
	 * 获取当前用户的名字
	 * @author Frodez
	 * @date 2019-12-29
	 */
	public static String name() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

	/**
	 * 获取当前用户的UserBaseInfo,未登录会返回null
	 * @author Frodez
	 * @date 2019-12-29
	 */
	public static UserBaseInfo user() {
		String token = token();
		if (token == null) {
			return null;
		}
		Long id = idTokenCache.getId(token);
		if (id == null) {
			return null;
		}
		return userCache.get(id);
	}

	/**
	 * 获取当前用户的Role,未登录会返回null
	 * @author Frodez
	 * @date 2019-12-29
	 */
	public static Role role() {
		String token = token();
		if (token == null) {
			return null;
		}
		Long id = idTokenCache.getId(token);
		if (id == null) {
			return null;
		}
		return roleCache.get(id);
	}

	/**
	 * 获取当前用户的UserInfo,未登录会返回null
	 * @author Frodez
	 * @date 2019-12-29
	 */
	public static UserInfo now() {
		String token = token();
		if (token == null) {
			return null;
		}
		Long id = idTokenCache.getId(token);
		if (id == null) {
			return null;
		}
		UserBaseInfo user = userCache.get(id);
		if (user == null) {
			return null;
		}
		Role role = roleCache.get(id);
		if (role == null) {
			return null;
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setUser(user);
		userInfo.setRole(role);
		return userInfo;
	}

	/**
	 * 判断当前用户的某一条设置是否关闭
	 * @author Frodez
	 * @date 2019-12-29
	 */
	public static boolean pass(String setting) {
		Role role = role();
		if (role == null) {
			throw new IllegalStateException("当前用户未登录!");
		}
		return settingCache.pass(role.getId(), setting);
	}

	/**
	 * 判断当前用户的某一条设置是否关闭
	 * @author Frodez
	 * @date 2019-12-29
	 */
	public static boolean reject(String setting) {
		Role role = role();
		if (role == null) {
			throw new IllegalStateException("当前用户未登录!");
		}
		return settingCache.reject(role.getId(), setting);
	}

}
