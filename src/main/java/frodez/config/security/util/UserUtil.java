package frodez.config.security.util;

import frodez.dao.result.user.UserInfo;
import frodez.service.cache.vm.facade.TokenCache;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.result.Result;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.MVCUtil;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 登录用户信息获取工具类
 * @author Frodez
 * @date 2019-01-09
 */
@Component
@DependsOn("contextUtil")
public class UserUtil {

	private static TokenCache tokenCache;

	private static IAuthorityService authorityService;

	@PostConstruct
	private void init() {
		authorityService = ContextUtil.bean(IAuthorityService.class);
		tokenCache = ContextUtil.bean(TokenCache.class);
		Assert.notNull(authorityService, "authorityService must not be null");
		Assert.notNull(tokenCache, "tokenCache must not be null");
	}

	/**
	 * 获取当前用户信息,不能用于免验证URI中
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static UserInfo get() {
		HttpServletRequest request = MVCUtil.request();
		if (!Matcher.needVerify(request.getRequestURI())) {
			throw new RuntimeException("不能在免验证URI中获取token信息!");
		}
		return tokenCache.get(TokenUtil.getRealToken(request));
	}

	/**
	 * 根据用户id查询用户信息,以UserInfo形式返回,功能同UserUtil.find(userId)
	 * @see frodez.config.security.util.UserUtil#find(Long)
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static UserInfo get(Long userId) {
		Result result = authorityService.getUserInfo(userId);
		if (result.unable()) {
			return null;
		}
		return result.as(UserInfo.class);
	}

	/**
	 * 根据用户名查询用户信息,以UserInfo形式返回,功能同UserUtil.find(userName)
	 * @see frodez.config.security.util.UserUtil#find(String)
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static UserInfo get(String userName) {
		Result result = authorityService.getUserInfo(userName);
		if (result.unable()) {
			return null;
		}
		return result.as(UserInfo.class);
	}

	/**
	 * 根据用户id查询用户信息,以Result形式返回,功能同UserUtil.get(userId)
	 * @see frodez.config.security.util.UserUtil#get(Long)
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static Result find(Long userId) {
		return authorityService.getUserInfo(userId);
	}

	/**
	 * 根据用户名查询用户信息,以Result形式返回,功能同UserUtil.get(userName)
	 * @see frodez.config.security.util.UserUtil#get(String)
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static Result find(String userName) {
		return authorityService.getUserInfo(userName);
	}

}
