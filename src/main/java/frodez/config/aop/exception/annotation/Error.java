package frodez.config.aop.exception.annotation;

import frodez.constant.errors.code.ErrorCode;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 错误码,与@CatchAndThrow配合使用<br>
 * 优先采用方法上的错误码配置,若无配置则采用类上的错误码配置
 * @author Frodez
 * @date 2019-12-09
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Error {

	ErrorCode value();

}
