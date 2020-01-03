package frodez.config.aop.validation.annotation.special;

import frodez.constant.converters.HttpMethodReverter;
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
 * 验证是否满足HttpMethod的字段要求
 * @author Frodez
 * @date 2019-12-31
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Methods.Validator.class)
public @interface Methods {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 枚举验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<Methods, Short> {

		@Override
		public boolean isValid(Short value, ConstraintValidatorContext context) {
			return HttpMethodReverter.revert(value) != null;
		}

	}

}
