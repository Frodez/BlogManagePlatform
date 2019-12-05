package frodez.config.swagger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 成功返回参数
 * @author Frodez
 * @date 2019-12-05
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Success {

	/**
	 * 返回类型
	 * @author Frodez
	 * @date 2019-12-05
	 */
	Class<?> value();

	/**
	 * 包装类型<br>
	 * <strong>不建议引入过于复杂的嵌套类型。如果需要,请使用一个特殊的类来封装嵌套类型。<br>
	 * 如List(List(Integer))这种类型,最好用类型Collections来封装内层的List(Integer),然后把类型变为List(Collections)。<br>
	 * 这样可以便于理解数据结构。 </strong>
	 * @author Frodez
	 * @date 2019-12-05
	 */
	ContainerType containerType() default ContainerType.NONE;

	/**
	 * 包装类型<br>
	 * <strong>不建议引入过于复杂的嵌套类型。如果需要,请使用一个特殊的类来封装嵌套类型。<br>
	 * 如List(List(Integer))这种类型,最好用类型Collections来封装内层的List(Integer),然后把类型变为List(Collections)。<br>
	 * 这样可以便于理解数据结构。 </strong>
	 * @author Frodez
	 * @date 2019-12-05
	 */
	public enum ContainerType {
		/**
		 * 无包装
		 */
		NONE,
		/**
		 * 分页封装,详情见PageData
		 * @see frodez.util.beans.result.PageData
		 */
		PAGE,
		/**
		 * 普通list
		 */
		LIST,
		/**
		 * 普通set
		 */
		SET;
	}

}
