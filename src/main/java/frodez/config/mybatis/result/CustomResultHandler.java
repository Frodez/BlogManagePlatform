package frodez.config.mybatis.result;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义mybatis ResultHandler注解<br>
 * @author Frodez
 * @date 2019-12-27
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomResultHandler {

	/**
	 * 自定义的ResultHandler类型,请注意返回值类型的处理
	 * @see frodez.config.mybatis.result.CustomHandler#support(java.util.List)
	 * @see frodez.config.mybatis.result.CustomHandler#getResult()
	 * @author Frodez
	 * @date 2019-12-27
	 */
	Class<? extends CustomHandler> value();

}
