package frodez.config.aop.request;

import com.google.common.util.concurrent.RateLimiter;
import frodez.config.aop.request.annotation.Limit;
import frodez.util.aop.AspectUtil;
import frodez.util.spring.context.ContextUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 请求限流AOP
 * @author Frodez
 * @date 2019-03-06
 */
@Aspect
@Component
public class LimitUserAOP {

	private Map<String, RateLimiter> limitCache = new ConcurrentHashMap<>();

	@Before("@annotation(frodez.config.aop.request.annotation.RepeatLock)")
	public void limit(JoinPoint point) {
		String url = ContextUtil.request().getRequestURI();
		RateLimiter limiter = limitCache.get(url);
		if (limiter == null) {
			limiter = RateLimiter.create(AspectUtil.annotation(point, Limit.class).value());
			limitCache.put(url, limiter);
		}
		limiter.acquire();
	}

}
