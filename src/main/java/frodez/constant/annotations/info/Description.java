package frodez.constant.annotations.info;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对象描述
 * @author Frodez
 * @date 2019-12-08
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE,
	ElementType.ANNOTATION_TYPE, ElementType.PACKAGE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE, ElementType.MODULE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

	/**
	 * 对象名
	 * @author Frodez
	 * @date 2019-12-08
	 */
	String name() default "";

	/**
	 * 作者名
	 * @author Frodez
	 * @date 2019-12-08
	 */
	String author() default "";

	/**
	 * 描述
	 * @author Frodez
	 * @date 2019-12-08
	 */
	String description() default "";

	/**
	 * 创建日期
	 * @author Frodez
	 * @date 2019-12-08
	 */
	String date() default "";

	/**
	 * 最后一次修改日期
	 * @author Frodez
	 * @date 2019-12-08
	 */
	String modDate() default "";

}
