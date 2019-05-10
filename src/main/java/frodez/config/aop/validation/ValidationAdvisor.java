package frodez.config.aop.validation;

import frodez.config.aop.validation.annotation.Check;
import frodez.util.beans.result.Result;
import frodez.util.common.ValidationUtil;
import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 验证参数AOP<br>
 * 使用方法:在方法的实现上加入@check注解,然后在方法实现的请求参数上使用hibernate validation api支持的方式配置验证.<br>
 * @author Frodez
 * @date 2019-01-12
 */
@Component
@Order(Integer.MAX_VALUE)
public class ValidationAdvisor implements PointcutAdvisor {

	@Override
	public Advice getAdvice() {
		return (MethodInterceptor) invocation -> {
			String msg = ValidationUtil.validateParam(invocation.getThis(), invocation.getMethod(), invocation
				.getArguments());
			return msg == null ? invocation.proceed() : Result.errorRequest(msg);
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
						if (method.getAnnotation(Check.class) == null) {
							return false;
						}
						if (method.getParameterCount() == 0) {
							throw new IllegalArgumentException("本注解不能在无参数的方法上使用!");
						}
						if (method.getReturnType() != Result.class) {
							throw new IllegalArgumentException("本方法的返回值类型必须为" + Result.class.getName());
						}
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
						if (method.getAnnotation(Check.class) == null) {
							return false;
						}
						if (method.getParameterCount() == 0) {
							throw new IllegalArgumentException("本注解不能在无参数的方法上使用!");
						}
						if (method.getReturnType() != Result.class) {
							throw new IllegalArgumentException("本方法的返回值类型必须为" + Result.class.getName());
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
