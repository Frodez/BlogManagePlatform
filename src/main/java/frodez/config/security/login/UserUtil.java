package frodez.config.security.login;

import frodez.config.security.util.TokenManager;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.vm.facade.TokenCache;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.result.Result;
import frodez.util.http.URLMatcher;
import frodez.util.spring.context.ContextUtil;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

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
		authorityService = ContextUtil.get(IAuthorityService.class);
		tokenCache = ContextUtil.get(TokenCache.class);
		Objects.requireNonNull(authorityService);
		Objects.requireNonNull(tokenCache);
	}

	/**
	 * 获取当前用户信息,不能用于免验证URI中
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static UserInfo get() {
		HttpServletRequest request = ContextUtil.request();
		if (!URLMatcher.needVerify(request.getRequestURI())) {
			throw new RuntimeException("不能在免验证URI中获取token信息!");
		}
		return tokenCache.get(TokenManager.getRealToken(request));
	}

	public static UserInfo get(Long userId) {
		Result result = authorityService.getUserInfo(userId);
		if (result.unable()) {
			return null;
		}
		return result.as(UserInfo.class);
	}

	public static UserInfo get(String userName) {
		Result result = authorityService.getUserInfo(userName);
		if (result.unable()) {
			return null;
		}
		return result.as(UserInfo.class);
	}

	public static Result find(Long userId) {
		return authorityService.getUserInfo(userId);
	}

	public static Result find(String userName) {
		return authorityService.getUserInfo(userName);
	}

}
