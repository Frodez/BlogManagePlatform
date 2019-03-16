package frodez.config.aop.request;

import com.google.common.util.concurrent.RateLimiter;
import frodez.config.aop.request.annotation.Limit;
import frodez.util.aop.AspectUtil;
import frodez.util.beans.result.Result;
import frodez.util.constant.setting.DefTime;
import frodez.util.spring.context.ContextUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

	@Around("@annotation(frodez.config.aop.request.annotation.Limit)")
	public Object limit(ProceedingJoinPoint point) throws Throwable {
		Limit limit = AspectUtil.annotation(point, Limit.class);
		RateLimiter limiter = limitCache.computeIfAbsent(ContextUtil.request().getRequestURI(), i -> RateLimiter.create(
			limit.value()));
		if (!limiter.tryAcquire(limit.timeout(), DefTime.UNIT)) {
			return Result.busy();
		}
		return point.proceed();
	}

}
