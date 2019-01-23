package frodez.config.aop.request.checker.impl;

import frodez.config.aop.request.checker.facade.RepeatChecker;
import frodez.config.aop.request.checker.facade.ServletKeyGenerator;
import frodez.config.security.settings.SecurityProperties;
import frodez.constant.redis.Redis;
import frodez.service.redis.RedisService;
import frodez.util.http.HttpUtil;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 阻塞型重复请求检查实现
 * @author Frodez
 * @date 2019-01-21
 */
@Component
public class RepeatCheckerImpl implements RepeatChecker, ServletKeyGenerator {
	
	/**
	 * redis服务
	 */
	@Autowired
	private RedisService redisService;
	
	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	@Override
	public boolean check(String key) {
		return redisService.exists(key);
	}

	@Override
	public void lock(String key) {
		redisService.set(key, true);
	}

	@Override
	public void free(String key) {
		redisService.delete(key);
	}

	@Override
	public String getKey(String sault, HttpServletRequest request) {
		if (properties.needVerify(request.getRequestURI())) {
			// 非登录接口使用token判断,同一token不能重复请求
			String fullToken = request.getHeader(properties.getJwt().getHeader());
			fullToken = fullToken == null ? "" : fullToken;
			return Redis.Request.NO_REPEAT + sault + fullToken;
		} else {
			// 登录接口使用IP判断,同一IP不能重复请求
			String address = HttpUtil.getAddr(request);
			return Redis.Request.NO_REPEAT + address;
		}
	}

}
