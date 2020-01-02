package frodez.config.aop.request.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import lombok.experimental.UtilityClass;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 控制重复请求注解,只用于controller中的端点<br>
 * 不能同时在类上同时拥有TimeoutLock和RepeatLock注解。方法上也遵循这一规则。<br>
 * @author Frodez
 * @date 2018-12-21
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatLock {

	@UtilityClass
	public static class RepeatLockHelper {

		public static RepeatLock get(Method method, Class<?> targetClass) {
			RepeatLock annotation = AnnotationUtils.findAnnotation(method, RepeatLock.class);
			if (annotation == null) {
				annotation = AnnotationUtils.findAnnotation(targetClass, RepeatLock.class);
				if (annotation != null) {
					if (AnnotationUtils.findAnnotation(targetClass, TimeoutLock.class) != null) {
						throw new IllegalStateException("不能在同一个类上同时放置TimeoutLock和RepeatLock注解");
					}
				}
			} else {
				if (AnnotationUtils.findAnnotation(method, TimeoutLock.class) != null) {
					throw new IllegalStateException("不能在同一个方法上同时放置TimeoutLock和RepeatLock注解");
				}
			}
			return annotation;
		}

	}

}
