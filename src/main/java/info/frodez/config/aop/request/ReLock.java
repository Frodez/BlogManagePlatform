package info.frodez.config.aop.request;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制重复请求注解
 * @author Frodez
 * @date 2018-12-21
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReLock {

	// 别名,详见info.frodez.constant.redis.Repeat
	String value() default "";

}
