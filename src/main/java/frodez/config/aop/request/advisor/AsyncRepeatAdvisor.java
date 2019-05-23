package frodez.config.aop.request.advisor;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.aop.request.checker.facade.ManualChecker;
import frodez.config.aop.request.checker.impl.KeyGenerator;
import frodez.config.aop.util.AOPUtil;
import frodez.util.beans.result.Result;
import frodez.util.http.ServletUtil;
import frodez.util.reflect.ReflectUtil;
import frodez.util.spring.MVCUtil;
import java.lang.reflect.Method;
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
 * 如果存在对应value,说明出现重复请求,返回重复请求信息.<br>
 * 如果不存在对应value,说明没有重复请求,继续执行.<br>
 * 请求处理方法结束后,根据规则获得key,然后删除对应key.<br>
 * @author Frodez
 * @date 2018-12-21
 */
@Slf4j
@Component
public class AsyncRepeatAdvisor implements PointcutAdvisor {

	/**
	 * 阻塞型重复请求检查
	 */
	@Autowired
	@Qualifier("repeatGuavaChecker")
	private ManualChecker checker;

	/**
	 * AOP切点
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public Advice getAdvice() {
		/**
		 * 在请求前判断是否存在正在执行的请求
		 * @param JoinPoint AOP切点
		 * @author Frodez
		 * @throws Throwable
		 * @date 2018-12-21
		 */
		return (MethodInterceptor) invocation -> {
			HttpServletRequest request = MVCUtil.request();
			String key = KeyGenerator.servletKey(ReflectUtil.getFullMethodName(invocation.getMethod()), request);
			try {
				if (checker.check(key)) {
					log.info("重复请求:IP地址{}", ServletUtil.getAddr(request));
					return Result.repeatRequest().async();
				}
				checker.lock(key);
				return invocation.proceed();
			} finally {
				checker.free(key);
			}
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
						if (method.getAnnotation(RepeatLock.class) == null) {
							return false;
						}
						if (!AOPUtil.isAsyncResultAsReturn(method)) {
							return false;
						}
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
