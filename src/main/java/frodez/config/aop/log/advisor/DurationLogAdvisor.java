package frodez.config.aop.log.advisor;

import frodez.config.aop.log.annotation.DurationLog;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.reflect.ReflectUtil;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 日志管理AOP切面
 * @author Frodez
 * @date 2019-01-12
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class DurationLogAdvisor implements PointcutAdvisor {

	/**
	 * 注解配置缓存
	 */
	private Map<String, Long> thresholdCache = new HashMap<>();

	private long times = 1000 * 1000;

	/**
	 * AOP切点
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public Advice getAdvice() {
		/**
		 * 检测出耗时过高的方法调用并在日志中告警
		 * @param JoinPoint AOP切点
		 * @author Frodez
		 * @throws Throwable
		 * @date 2018-12-21
		 */
		return (MethodInterceptor) invocation -> {
			String name = ReflectUtil.fullName(invocation.getMethod());
			long threshold = thresholdCache.get(name);
			long count = System.nanoTime();
			Object result = invocation.proceed();
			count = System.nanoTime() - count;
			if (count > threshold) {
				log.warn("{}方法耗时{}毫秒,触发超时警告!", name, count / times);
			}
			return result;
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
						DurationLog annotation = AnnotationUtils.findAnnotation(method, DurationLog.class);
						if (annotation == null) {
							return false;
						}
						if (annotation.threshold() <= 0) {
							throw new CodeCheckException("方法", ReflectUtil.fullName(method), "的阈值必须大于0!");
						}
						thresholdCache.put(ReflectUtil.fullName(method), annotation.threshold() * times);
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
