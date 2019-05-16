package frodez.util.constant.annotation.decoration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 本注解标明的类只针对于枚举类,表明该类可以使用LegalEnum注解来进行验证。
 * @see frodez.config.aop.validation.annotation.common.LegalEnum
 * @author Frodez
 * @date 2019-03-18
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface EnumCheckable {

}
