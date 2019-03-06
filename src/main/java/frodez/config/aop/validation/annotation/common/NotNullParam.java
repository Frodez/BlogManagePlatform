package frodez.config.aop.validation.annotation.common;

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
 * 方法请求参数非空验证注解<br>
 * @author Frodez
 * @date 2019-03-01
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullParam.Validator.class)
public @interface NotNullParam {

	// 错误信息
	String message() default "请求参数不能为空!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 非空验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<NotNullParam, Object> {

		/**
		 * 根据注解信息初始化非空验证器
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public void initialize(NotNullParam enumValue) {
		}

		/**
		 * 验证
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
			return value != null;
		}

	}

}
