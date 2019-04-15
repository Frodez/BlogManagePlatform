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
import javax.validation.constraints.Pattern.Flag;

/**
 * 字符串验证注解
 * @author Frodez
 * @date 2019-03-03
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LegalStr.Validator.class)
public @interface LegalStr {

	/**
	 * 错误信息,默认为"字符串格式错误!"
	 * @author Frodez
	 * @date 2019-04-13
	 */
	String message() default "字符串格式错误!";

	/**
	 * 使用的正则表达式
	 * @author Frodez
	 * @date 2019-04-13
	 */
	String regex();

	Flag[] flags() default {};

	/**
	 * 是否允许null,默认为false不允许
	 * @author Frodez
	 * @date 2019-04-13
	 */
	boolean nullable() default false;

	/**
	 * 格式验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<LegalStr, String> {

		/**
		 * 验证用的格式
		 */
		private String regex;

		private int flag;

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
		public void initialize(LegalStr enumValue) {
			regex = enumValue.regex();
			flag = RegexUtil.transfer(enumValue.flags());
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
			return RegexUtil.match(regex, value, flag);
		}

	}

}
