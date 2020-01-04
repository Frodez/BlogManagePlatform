package frodez.config.aop.exception.advisor;

import frodez.config.aop.exception.ExceptionProperties;
import frodez.config.aop.exception.annotation.CatchAndThrow;
import frodez.config.aop.exception.annotation.Error;
import frodez.constant.errors.code.ErrorCode;
import frodez.constant.errors.code.ServiceException;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.common.StrUtil;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class CatchAndThrowAdvisor implements PointcutAdvisor {

	private Map<String, ErrorCode> errorCodeCache = new HashMap<>();

	@Override
	public Advice getAdvice() {
		return (MethodInterceptor) invocation -> {
			try {
				return invocation.proceed();
			} catch (Exception e) {
				String methodName = ReflectUtil.fullName(invocation.getMethod());
				log.error(StrUtil.concat("[", methodName, "]"), e);
				throw new ServiceException(errorCodeCache.get(methodName));
			}
		};
	}

	@Override
	public boolean isPerInstance() {
		return true;
	}

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
						if (ExceptionProperties.autoConfig) {
							if (AnnotationUtils.findAnnotation(targetClass, Error.class) == null) {
								return false;
							}
							if (AnnotationUtils.findAnnotation(method, Transactional.class) != null) {
								String methodName = ReflectUtil.fullName(method);
								errorCodeCache.put(methodName, resolveErrorCode(method, targetClass));
								return true;
							} else {
								return false;
							}
						}
						//这里可以进行运行前检查
						CatchAndThrow annotation = AnnotationUtils.findAnnotation(method, CatchAndThrow.class);
						if (annotation == null) {
							return false;
						}
						String methodName = ReflectUtil.fullName(method);
						errorCodeCache.put(methodName, resolveErrorCode(method, targetClass));
						return true;
					}

					private ErrorCode resolveErrorCode(Method method, Class<?> targetClass) {
						Error error = AnnotationUtils.findAnnotation(method, Error.class);
						if (error != null) {
							return error.value();
						}
						error = AnnotationUtils.findAnnotation(targetClass, Error.class);
						if (error != null) {
							return error.value();
						}
						String string = StrUtil.concat("方法", ReflectUtil.fullName(method), "或者类", targetClass.getName(), "上必须存在@Error注解!");
						throw new CodeCheckException(string);
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
