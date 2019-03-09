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
		RateLimiter limiter = limitCache.computeIfAbsent(ContextUtil.request().getRequestURI(), i -> RateLimiter.create(
			AspectUtil.annotation(point, Limit.class).value()));
		//默认请求时长的100倍
		if (!limiter.tryAcquire((long) (100 * 1000 / limiter.getRate()), DefTime.UNIT)) {
			return Result.fail("请求超时");
		}
		return point.proceed();
	}

}
