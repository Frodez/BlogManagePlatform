package frodez.config.aop.log.advisor;

import frodez.config.aop.log.annotation.ResultLog;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.json.JSONUtil;
import frodez.util.reflect.ReflectUtil;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;
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
public class ResultLogAdvisor implements PointcutAdvisor {

	/**
	 * AOP切点
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public Advice getAdvice() {
		/**
		 * 打印返回值到日志
		 * @param JoinPoint AOP切点
		 * @author Frodez
		 * @date 2019-01-12
		 */
		return (AfterReturningAdvice) (returnValue, method, args, target) -> log.info("{} 返回值:{}", ReflectUtil.fullName(method), JSONUtil
			.string(returnValue));
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
						if (AnnotationUtils.findAnnotation(method, ResultLog.class) == null) {
							return false;
						}
						Class<?> returnType = method.getReturnType();
						if (ListenableFuture.class.isAssignableFrom(returnType)) {
							return false;
						}
						if (returnType == Void.class) {
							throw new CodeCheckException("不能对void返回类型的方法", ReflectUtil.fullName(method), "使用@", ResultLog.class
								.getCanonicalName(), "注解!");
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
