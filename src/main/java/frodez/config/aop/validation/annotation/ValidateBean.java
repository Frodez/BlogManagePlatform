package frodez.config.aop.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用于标注需要进行hibernate-validator相关代码规范检查的类。无业务作用。<br>
 * 建议至少在param实体中使用本注解。<br>
 * @author Frodez
 * @date 2019-06-04
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateBean {

}
