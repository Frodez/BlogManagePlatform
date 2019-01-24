package frodez.config.aop.request.checker.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import frodez.config.aop.request.checker.facade.RepeatChecker;
import frodez.service.redis.RedisService;

/**
 * 阻塞型重复请求检查实现
 * @author Frodez
 * @date 2019-01-21
 */
@Component
public class RepeatCheckerImpl implements RepeatChecker {
	
	/**
	 * redis服务
	 */
	@Autowired
	private RedisService redisService;

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

}
