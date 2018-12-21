package info.frodez.config.aop.request;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import info.frodez.util.redis.RedisService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class NoRepeatAop {

	@Autowired
	private RedisService redisService;

}
