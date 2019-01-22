package info.frodez.config.aop.request.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制重复请求注解(带过期时间),只用于controller中的端点
 * @author Frodez
 * @date 2018-12-21
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeoutLock {

	// 别名,详见info.frodez.constant.redis.Repeat
	String value() default "";

	// 过期时间,大于等于0,单位毫秒,默认值500
	long time() default 500;

}
