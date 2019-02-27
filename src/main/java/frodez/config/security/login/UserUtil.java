package frodez.config.security.login;

import frodez.config.security.login.cache.facade.TokenCache;
import frodez.config.security.settings.SecurityProperties;
import frodez.service.user.result.UserInfo;
import frodez.util.spring.context.ContextUtil;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录用户信息获取工具类
 * @author Frodez
 * @date 2019-01-09
 */
@Component
public class UserUtil {

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	@Autowired
	private TokenCache tokenCache;

	private static UserUtil userManager;

	@PostConstruct
	private void init() {
		userManager = this;
	}

	private UserInfo getUserInfo() {
		HttpServletRequest request = ContextUtil.getRequest();
		if (!properties.needVerify(request.getRequestURI())) {
			throw new RuntimeException("不能在免验证URI中获取token信息!");
		}
		return tokenCache.get(properties.getRealToken(request));
	}

	/**
	 * 获取当前用户信息,不能用于免验证URI中
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static UserInfo get() {
		return userManager.getUserInfo();
	}

}
