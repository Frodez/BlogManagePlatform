package frodez.config.security.login;

import frodez.config.security.settings.SecurityProperties;
import frodez.constant.redis.Redis;
import frodez.dao.result.user.UserInfo;
import frodez.service.redis.RedisService;
import frodez.util.json.JSONUtil;
import frodez.util.spring.context.ContextUtil;
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
		HttpServletRequest request = ContextUtil.getRequest();
		if (!properties.needVerify(request.getRequestURI())) {
			throw new RuntimeException("不能在免验证URI中获取token信息!");
		}
		String fullToken = request.getHeader(properties.getJwt().getHeader());
		String userName = redisService.getString(Redis.User.TOKEN + fullToken);
		String json = redisService.getString(Redis.User.BASE_INFO + userName);
		return JSONUtil.toObject(json, UserInfo.class);
	}

}
