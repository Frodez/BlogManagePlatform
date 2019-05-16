package frodez.util.constant.annotation.decoration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 本注解标明的方法或者类下的所有方法,不要用于controller中，只能用于内部调用或者远程调用。
 * @author Frodez
 * @date 2019-03-18
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface ServiceOnly {

}
