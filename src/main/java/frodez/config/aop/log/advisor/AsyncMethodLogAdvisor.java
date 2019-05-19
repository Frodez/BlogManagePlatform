package frodez.config.aop.log.advisor;

import frodez.config.aop.log.annotation.MethodLog;
import frodez.util.beans.result.Result;
import frodez.util.json.JSONUtil;
import frodez.util.reflect.ReflectUtil;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 日志管理AOP切面
 * @author Frodez
 * @date 2019-01-12
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class AsyncMethodLogAdvisor implements PointcutAdvisor {

	/**
	 * AOP切点
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Advice getAdvice() {
		return (MethodInterceptor) invocation -> {
			Method method = invocation.getMethod();
			String name = ReflectUtil.getFullMethodName(method);
			if (method.getParameterCount() != 0) {
				Parameter[] parameters = method.getParameters();
				Object[] args = invocation.getArguments();
				Map<String, Object> paramMap = new HashMap<>(parameters.length);
				for (int i = 0; i < parameters.length; ++i) {
					paramMap.put(parameters[i].getName(), args[i]);
				}
				log.info("{} 请求参数:{}", name, JSONUtil.string(paramMap));
			} else {
				log.info("{} 本方法无入参", name);
			}
			Object result = invocation.proceed();
			if (method.getReturnType() != Void.class) {
				if (result != null) {
					log.info("{} 返回值:{}", name, JSONUtil.string(((ListenableFuture<Result>) result).get()));
				} else {
					log.info("{} 返回值:{}", name, JSONUtil.string(result));
				}
			} else {
				log.info("{} 本方法返回值类型为void", name);
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
						if (method.getAnnotation(MethodLog.class) == null) {
							return false;
						}
						Class<?> returnType = method.getReturnType();
						if (returnType == Result.class) {
							return false;
						}
						if (returnType == Void.class && method.getParameterCount() == 0) {
							throw new IllegalArgumentException("不能对void返回类型且无参数的方法使用本注解!");
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
