package frodez.config.aop.log.advisor;

import frodez.config.aop.log.annotation.ParamLog;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.json.JSONUtil;
import frodez.util.reflect.ReflectUtil;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodBeforeAdvice;
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
public class ParamLogAdvisor implements PointcutAdvisor {

	/**
	 * AOP切点
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public Advice getAdvice() {
		/**
		 * 打印参数到日志
		 * @param JoinPoint AOP切点
		 * @author Frodez
		 * @date 2019-01-12
		 */
		return (MethodBeforeAdvice) (method, args, target) -> {
			Parameter[] parameters = method.getParameters();
			Map<String, Object> paramMap = new HashMap<>(parameters.length);
			for (int i = 0; i < parameters.length; ++i) {
				paramMap.put(parameters[i].getName(), args[i]);
			}
			log.info("{} 请求参数:{}", ReflectUtil.fullName(method), JSONUtil.string(paramMap));
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
						if (AnnotationUtils.findAnnotation(method, ParamLog.class) == null) {
							return false;
						}
						if (method.getParameterCount() == 0) {
							throw new CodeCheckException("不能对无参数的方法", ReflectUtil.fullName(method), "使用@", ParamLog.class.getCanonicalName(),
								"注解!");
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
