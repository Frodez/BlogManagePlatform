package frodez.config.aop.validation.annotation.common;

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
import javax.validation.constraints.Pattern.Flag;

/**
 * 字符串验证注解
 * @author Frodez
 * @date 2019-03-03
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Match.Validator.class)
public @interface Match {

	/**
	 * 错误信息
	 * @author Frodez
	 * @date 2019-04-13
	 */
	String message() default "{frodez.config.aop.validation.annotation.common.Match.message}";

	/**
	 * 使用的正则表达式
	 * @author Frodez
	 * @date 2019-04-13
	 */
	String regex();

	/**
	 * 正则表达式的模式
	 * @see java.util.regex.Pattern
	 * @see javax.validation.constraints.Pattern.Flag
	 * @author Frodez
	 * @date 2019-04-18
	 */
	Flag[] flags() default {};

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 格式验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<Match, String> {

		/**
		 * 验证用的格式
		 */
		private String regex;

		/**
		 * 正则表达式的模式
		 */
		private int flag;

		/**
		 * 根据注解信息初始化验证器
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public void initialize(Match enumValue) {
			regex = enumValue.regex();
			flag = RegexUtil.transfer(enumValue.flags());
		}

		/**
		 * 验证
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
			if (value == null) {
				return true;
			}
			return RegexUtil.match(regex, value, flag);
		}

	}

}
