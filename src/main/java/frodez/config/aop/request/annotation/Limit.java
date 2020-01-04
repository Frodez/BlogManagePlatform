package frodez.config.aop.request.annotation;

import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.reflect.ReflectUtil;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import javax.validation.constraints.Positive;
import lombok.experimental.UtilityClass;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 请求限流注解
 * @author Frodez
 * @date 2018-12-21
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

	/**
	 * 每秒每token限制请求数,默认值100.0
	 * @author Frodez
	 * @date 2019-04-13
	 */
	@Positive
	double value() default 100.0;

	/**
	 * 超时时间,默认值3000毫秒
	 * @author Frodez
	 * @date 2019-04-13
	 */
	@Positive
	long timeout() default 3000;

	@UtilityClass
	public static class LimitHelper {

		public static Limit get(Method method, Class<?> targetClass) {
			Limit annotation = AnnotationUtils.findAnnotation(method, Limit.class);
			if (annotation == null) {
				annotation = AnnotationUtils.findAnnotation(targetClass, Limit.class);
			}
			return annotation;
		}

		public static void check(Method method, Limit annotation) {
			if (annotation.value() <= 0) {
				throw new CodeCheckException("方法", ReflectUtil.fullName(method), "的每秒每token限制请求数必须大于0!");
			}
			if (annotation.timeout() <= 0) {
				throw new CodeCheckException("方法", ReflectUtil.fullName(method), "的超时时间必须大于0!");
			}
		}
	}

}
