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
 * 控制重复请求注解(带过期时间),只用于controller中的端点<br>
 * 不能同时在类上同时拥有TimeoutLock和RepeatLock注解。方法上也遵循这一规则。<br>
 * @author Frodez
 * @date 2018-12-21
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeoutLock {

	/**
	 * 过期时间,大于等于0,单位毫秒,默认值500 <strong>如果设置了小于等于0的值,会在启动时抛出异常。</strong>
	 * @author Frodez
	 * @date 2019-04-13
	 */
	@Positive
	long value() default 500;

	@UtilityClass
	public static class TimeoutLockHelper {

		public static TimeoutLock get(Method method, Class<?> targetClass) {
			TimeoutLock annotation = AnnotationUtils.findAnnotation(method, TimeoutLock.class);
			if (annotation == null) {
				annotation = AnnotationUtils.findAnnotation(targetClass, TimeoutLock.class);
				if (annotation != null) {
					if (AnnotationUtils.findAnnotation(targetClass, RepeatLock.class) != null) {
						throw new IllegalStateException("不能在同一个类上同时放置TimeoutLock和RepeatLock注解");
					}
				}
			} else {
				if (AnnotationUtils.findAnnotation(method, RepeatLock.class) != null) {
					throw new IllegalStateException("不能在同一个方法上同时放置TimeoutLock和RepeatLock注解");
				}
			}
			return annotation;
		}

		public static void check(Method method, TimeoutLock annotation) {
			if (annotation.value() <= 0) {
				throw new CodeCheckException("方法", ReflectUtil.fullName(method), "的过期时间必须大于0!");
			}
		}

	}

}
