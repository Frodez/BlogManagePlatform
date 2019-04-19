package frodez.config.aop.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.constraints.Positive;

/**
 * 方法耗时监控注解
 * @author Frodez
 * @date 2019-01-12
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DurationLog {

	/**
	 * 警告触发阈值,默认3000毫秒
	 * @author Frodez
	 * @date 2019-04-13
	 */
	@Positive
	long threshold() default 3000;

}
