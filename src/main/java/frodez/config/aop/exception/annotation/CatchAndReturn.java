package frodez.config.aop.exception.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异常处理,截获所有异常,输出日志,然后返回错误消息码<br>
 * ExceptionProperties.autoConfig为true时,不需要配置此注解,而根据@Transactional注解判断。<br>
 * 不能在静态方法以及未在spring中注册的类实例中使用。<br>
 * 本注解无法处理finally块,请在方法体内处理finally<br>
 * 等同于:
 *
 * <pre>
 *
 * method XXX() {
 * 	try {
 * 	} catch (Exception e) {
 * 		log.error("[XXX]", e);
 * 		return Result.errorService();
 * 	}
 * }
 * </pre>
 *
 * @author Frodez
 * @date 2019-06-12
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CatchAndReturn {

}
