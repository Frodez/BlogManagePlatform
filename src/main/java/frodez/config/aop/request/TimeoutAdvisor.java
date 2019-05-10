package frodez.config.aop.request;

import frodez.config.aop.request.annotation.TimeoutLock;
import frodez.config.aop.request.checker.facade.AutoChecker;
import frodez.config.aop.request.checker.impl.KeyGenerator;
import frodez.util.beans.result.Result;
import frodez.util.http.ServletUtil;
import frodez.util.reflect.ReflectUtil;
import frodez.util.spring.MVCUtil;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 控制重复请求AOP切面<br>
 * <strong>原理:</strong><br>
 * 在请求处理方法前后设点.<br>
 * 进入请求处理方法前,根据规则获得key,然后查询是否存在对应value.<br>
 * 如果存在对应value,说明出现重复请求,抛出NoRepeatException异常.<br>
 * 如果不存在对应value,说明没有重复请求,继续执行.<br>
 * 请求处理方法结束后,根据规则获得key,然后删除对应key.<br>
 * @author Frodez
 * @date 2018-12-21
 */
@Slf4j
@Component
public class TimeoutAdvisor implements PointcutAdvisor {

	/**
	 * 自动超时型重复请求检查
	 */
	@Autowired
	@Qualifier("timeoutGuavaChecker")
	private AutoChecker checker;

	/**
	 * 注解配置缓存
	 */
	private Map<String, Long> timeoutCache = new ConcurrentHashMap<>();

	/**
	 * AOP切点
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public Advice getAdvice() {
		/**
		 * 在一定时间段内拦截重复请求
		 * @param JoinPoint AOP切点
		 * @author Frodez
		 * @date 2018-12-21
		 */
		return (MethodInterceptor) invocation -> {
			HttpServletRequest request = MVCUtil.request();
			String name = ReflectUtil.getFullMethodName(invocation.getMethod());
			String key = KeyGenerator.servletKey(name, request);
			if (checker.check(key)) {
				log.info("重复请求:IP地址{}", ServletUtil.getAddr(request));
				return Result.errorRequest();
			}
			checker.lock(key, timeoutCache.get(name));
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
						//这里可以进行运行前检查
						TimeoutLock annotation = method.getAnnotation(TimeoutLock.class);
						if (annotation == null) {
							return false;
						}
						if (annotation.value() <= 0) {
							throw new IllegalArgumentException("过期时间必须大于0!");
						}
						if (method.getReturnType() != Result.class) {
							throw new IllegalArgumentException("本方法的返回值类型必须为" + Result.class.getName());
						}
						timeoutCache.put(ReflectUtil.getFullMethodName(method), annotation.value());
						return true;
					}

					/**
					 * 对方法进行判断
					 * @author Frodez
					 * @date 2019-05-10
					 */
					@Override
					public boolean matches(Method method, Class<?> targetClass) {
						//这里可以进行运行前检查
						TimeoutLock annotation = method.getAnnotation(TimeoutLock.class);
						if (annotation == null) {
							return false;
						}
						if (annotation.value() <= 0) {
							throw new IllegalArgumentException("过期时间必须大于0!");
						}
						if (method.getReturnType() != Result.class) {
							throw new IllegalArgumentException("本方法的返回值类型必须为" + Result.class.getName());
						}
						timeoutCache.put(ReflectUtil.getFullMethodName(method), annotation.value());
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
