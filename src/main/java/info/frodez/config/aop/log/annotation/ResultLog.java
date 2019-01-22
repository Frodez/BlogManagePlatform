package info.frodez.config.aop.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 返回值日志注解
 * @author Frodez
 * @date 2019-01-12
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultLog {

}
