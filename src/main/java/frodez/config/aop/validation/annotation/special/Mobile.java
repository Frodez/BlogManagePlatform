package frodez.config.aop.validation.annotation.special;

import frodez.constant.settings.DefRegex;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号验证注解
 * @author Frodez
 * @date 2019-03-03
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Mobile.Validator.class)
public @interface Mobile {

	/**
	 * 错误信息,默认为"手机号格式错误!"
	 * @author Frodez
	 * @date 2019-04-13
	 */
	String message() default "{frodez.config.aop.validation.annotation.special.Mobile.message}";

	/**
	 * 格式验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<Mobile, String> {

		/**
		 * 验证用的格式
		 */
		private static Pattern pattern = DefRegex.MOBILE;

		/**
		 * 验证
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			if (value == null) {
				return true;
			}
			return pattern.matcher(value).matches();
		}

	}

}
