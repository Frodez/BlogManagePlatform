package frodez.config.aop.validation.annotation;

import frodez.util.common.RegexUtil;
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
 * 正则表达式验证注解 以下为注解参数说明:<br>
 * <strong>message: String类型,代表验证失败时的返回信息,默认值为"参数非法!"<br>
 * regex: String类型,验证用的正则表达式(不能为空!)<br>
 * nullable: boolean类型,代表对空值的处理方式,默认值为false.为true时空值可以通过验证,为false时空值不可以通过验证.<br>
 * </strong>
 * @author Frodez
 * @date 2019-03-01
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Match.Validator.class)
public @interface Match {

	// 错误信息
	String message() default "参数非法!";

	// 正则表达式
	String regex();

	// 是否可为空
	boolean nullable() default false;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 正则表达式验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<Match, String> {

		/**
		 * 正则表达式
		 */
		private String regex;

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
		public void initialize(Match enumValue) {
			regex = enumValue.regex();
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
			return RegexUtil.match(regex, value);
		}

	}

}
