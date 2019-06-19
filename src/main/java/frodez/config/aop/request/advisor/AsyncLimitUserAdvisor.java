package frodez.config.aop.request.advisor;

import com.google.common.util.concurrent.RateLimiter;
import frodez.config.aop.request.annotation.Limit;
import frodez.config.aop.util.AOPUtil;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.constant.settings.DefTime;
import frodez.util.beans.pair.Pair;
import frodez.util.beans.result.Result;
import frodez.util.reflect.ReflectUtil;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.stereotype.Component;

/**
 * 请求限流AOP
 * @author Frodez
 * @date 2019-03-06
 */
@Component
public class AsyncLimitUserAdvisor implements PointcutAdvisor {

	/**
	 * 限流器
	 */
	private Map<String, Pair<RateLimiter, Long>> limitCache = new HashMap<>();

	/**
	 * AOP切点
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public Advice getAdvice() {
		/**
		 * 请求限流
		 * @param JoinPoint AOP切点
		 * @author Frodez
		 * @date 2018-12-21
		 */
		return (MethodInterceptor) invocation -> {
			Pair<RateLimiter, Long> pair = limitCache.get(ReflectUtil.getFullMethodName(invocation.getMethod()));
			if (!pair.getKey().tryAcquire(pair.getValue(), DefTime.UNIT)) {
				return Result.busy().async();
			}
			return invocation.proceed();
		};
	}

	/**
	 * 默认true
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public boolean isPerInstance() {
		return true;
	}

	/**
	 * 切入点配置
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public Pointcut getPointcut() {
		return new Pointcut() {

			/**
			 * 根据方法判断
			 * @author Frodez
			 * @date 2019-05-10
			 */
			@Override
			public MethodMatcher getMethodMatcher() {
				return new MethodMatcher() {

					/**
					 * 对方法进行判断(运行时)
					 * @author Frodez
					 * @date 2019-05-10
					 */
					@Override
					public boolean matches(Method method, Class<?> targetClass, Object... args) {
						//isRuntime()方法返回值为false时,不会进行运行时判断
						return false;
					}

					/**
					 * 对方法进行判断
					 * @author Frodez
					 * @date 2019-05-10
					 */
					@Override
					public boolean matches(Method method, Class<?> targetClass) {
						//这里可以进行运行前检查
						Limit annotation = method.getAnnotation(Limit.class);
						if (annotation == null) {
							return false;
						}
						if (annotation.value() <= 0) {
							throw new CodeCheckException("方法", ReflectUtil.getFullMethodName(method),
								"的每秒每token限制请求数必须大于0!");
						}
						if (annotation.timeout() <= 0) {
							throw new CodeCheckException("方法", ReflectUtil.getFullMethodName(method), "的超时时间必须大于0!");
						}
						if (!AOPUtil.isAsyncResultAsReturn(method)) {
							return false;
						}
						Pair<RateLimiter, Long> pair = new Pair<>(RateLimiter.create(annotation.value()), annotation
							.timeout());
						limitCache.put(ReflectUtil.getFullMethodName(method), pair);
						return true;
					}

					/**
					 * 默认true
					 * @author Frodez
					 * @date 2019-05-10
					 */
					@Override
					public boolean isRuntime() {
						return false;
					}
				};
			}

			/**
			 * 根据类型判断
			 * @author Frodez
			 * @date 2019-05-10
			 */
			@Override
			public ClassFilter getClassFilter() {
				return clazz -> true;
			}

		};
	}

}
