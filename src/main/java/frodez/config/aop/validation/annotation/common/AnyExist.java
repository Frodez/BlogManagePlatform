package frodez.config.aop.validation.annotation.common;

import frodez.config.validator.ValidationUtil;
import frodez.util.common.EmptyUtil;
import frodez.util.common.StrUtil;
import frodez.util.reflect.ReflectUtil;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * 判断该类实体的指定字段是否至少有一个不为null
 * @author Frodez
 * @date 2019-12-29
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AnyExist.Validator.class)
public @interface AnyExist {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 指定的字段名,不设置的话则默认为所有字段
	 * @author Frodez
	 * @date 2019-12-29
	 */
	String[] value() default {};

	class Validator implements ConstraintValidator<AnyExist, Object> {

		private String[] fieldNames;

		private boolean isAll;

		@Override
		public void initialize(AnyExist annotation) {
			fieldNames = annotation.value();
			isAll = EmptyUtil.yes(fieldNames);
		}

		@Override
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			Class<?> klass = value.getClass();
			if (isAll) {
				for (Field field : klass.getDeclaredFields()) {
					if (ReflectUtil.tryGet(field, value) != null) {
						return true;
					}
				}
			} else {
				for (String fieldName : fieldNames) {
					if (ReflectUtil.tryGet(klass, fieldName, value) != null) {
						return true;
					}
				}
			}
			if (isAll) {
				ValidationUtil.changeMessage(context, StrUtil.concat(klass.getSimpleName(), "需要至少一个字段不为null"));
			} else {
				ValidationUtil.changeMessage(context, StrUtil.concat(klass.getSimpleName(), "的", String.join(", ", fieldNames), "字段需要至少一个字段不为null"));
			}
			return false;
		}

	}

}
