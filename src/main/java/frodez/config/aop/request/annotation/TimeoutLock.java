package frodez.config.aop.request.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.constraints.Positive;

/**
 * 控制重复请求注解(带过期时间),只用于controller中的端点
 * @author Frodez
 * @date 2018-12-21
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeoutLock {

	/**
	 * 过期时间,大于等于0,单位毫秒,默认值500 <strong>如果设置了小于等于0的值,会在启动时抛出异常。</strong>
	 * @author Frodez
	 * @date 2019-04-13
	 */
	@Positive
	long value() default 500;

}
