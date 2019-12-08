package frodez.config.swagger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举类型接口参数,替代@ApiParam
 * @author Frodez
 * @date 2019-12-08
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumParam {

	/**
	 * 适用的枚举类
	 * @author Frodez
	 * @date 2019-04-13
	 */
	Class<? extends Enum<?>> value();

	/**
	 * 获取所有枚举描述的方法名,默认为getDescs。
	 * @author Frodez
	 * @date 2019-12-08
	 */
	String descMethod() default "getDescs";

}
