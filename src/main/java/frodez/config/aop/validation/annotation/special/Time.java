package frodez.config.aop.validation.annotation.special;

import frodez.util.common.DateUtil;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * HH:mm:ss日期格式验证注解<br>
 * <strong>如果被验证的日期字符串在业务逻辑中需要被转换为date对象,请酌情考虑是否使用此注解.<br>
 * 因为本注解的原理也是尝试将字符串进行转换,转换失败则提示错误,如果使用此注解,意味着多了一次无谓的转换.<br>
 * </strong> 以下为注解参数说明:<br>
 * <strong> message: String类型,代表验证失败时的返回信息,默认值为"参数非法!"<br>
 * nullable: boolean类型,代表对空值的处理方式,默认值为false.为true时空值可以通过验证,为false时空值不可以通过验证.<br>
 * </strong>
 * @author Frodez
 * @date 2019-03-01
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Time.Validator.class)
public @interface Time {

	// 错误信息
	String message() default "参数非法!";

	// 是否可为空
	boolean nullable() default false;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 日期符合格式验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<Time, String> {

		/**
		 * 接受空值,默认值为false true:当为空时,直接通过验证 false:当为空时,拒绝通过验证
		 */
		private boolean nullable;

		/**
		 * 根据注解信息初始化验证器
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public void initialize(Time enumValue) {
			nullable = enumValue.nullable();
		}

		/**
		 * 验证
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
			if (value == null) {
				return nullable;
			}
			return DateUtil.isTime(value);
		}

	}

}
