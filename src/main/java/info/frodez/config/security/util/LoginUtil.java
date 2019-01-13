package info.frodez.config.security.util;

import info.frodez.config.security.settings.SecurityProperties;
import info.frodez.constant.redis.Redis;
import info.frodez.dao.result.user.UserInfo;
import info.frodez.service.redis.RedisService;
import info.frodez.util.json.JSONUtil;
import info.frodez.util.spring.context.ContextUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录用户信息获取工具类
 * @author Frodez
 * @date 2019-01-09
 */
@Component
public class LoginUtil {

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	/**
	 * redis服务
	 */
	@Autowired
	private RedisService redisService;

	/**
	 * 获取当前用户信息,不能用于免验证URI中
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public UserInfo getInfo() {
		String userName = redisService.getString(Redis.User.TOKEN + getToken());
		String json = redisService.getString(Redis.User.BASE_INFO + userName);
		return JSONUtil.toObject(json, UserInfo.class);
	}

	/**
	 * 获取当前用户token,不能用于免验证URI中
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public String getToken() {
		HttpServletRequest request = ContextUtil.getRequest();
		if (!properties.needVerify(request.getRequestURI())) {
			throw new RuntimeException("不能在免验证URI中获取token信息!");
		}
		return request.getHeader(properties.getJwt().getHeader());
	}

}
