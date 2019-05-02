package frodez.config.aop.request.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.constraints.Positive;

/**
 * 请求限流注解
 * @author Frodez
 * @date 2018-12-21
 */
@Documented
@Target({ ElementType.METHOD })
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

}
