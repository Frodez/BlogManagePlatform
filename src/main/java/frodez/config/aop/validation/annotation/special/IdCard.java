package frodez.config.aop.validation.annotation.special;

import frodez.util.constant.setting.DefRegex;
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
 * 身份证号验证注解
 * @author Frodez
 * @date 2019-03-03
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdCard.Validator.class)
public @interface IdCard {

	// 错误信息
	String message() default "身份证号格式错误!";

	// 是否可为空
	boolean nullable() default false;

	/**
	 * 格式验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<IdCard, String> {

		/**
		 * 验证用的格式
		 */
		private static Pattern pattern = Pattern.compile(DefRegex.IDCARD);

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
		public void initialize(IdCard enumValue) {
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
			return pattern.matcher(value).matches();
		}

	}

}
