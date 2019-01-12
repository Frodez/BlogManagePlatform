package info.frodez.config.security.impl.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import info.frodez.config.security.settings.SecurityProperties;
import info.frodez.constant.redis.Redis;
import info.frodez.dao.result.user.UserInfo;
import info.frodez.service.redis.RedisService;
import info.frodez.util.http.HttpUtil;
import info.frodez.util.json.JSONUtil;

/**
 * 用户信息获取工具类
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
	 * 获取当前用户信息(可能为空)
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public UserInfo getInfo() {
		String token = HttpUtil.getContextRequest().getHeader(properties.getJwt().getHeader());
		if (token == null) {
			return null;
		}
		String userName = redisService.getString(Redis.User.TOKEN + token);
		if (userName == null) {
			return null;
		}
		String json = redisService.getString(Redis.User.BASE_INFO + userName);
		return JSONUtil.toObject(json, UserInfo.class);
	}

}
