package frodez.config.aop.validation.annotation.special;

import frodez.util.beans.param.QueryPage;
import frodez.util.constant.setting.DefPage;
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
 * QueryPage校验
 * @author Frodez
 * @date 2019-05-16
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidQueryPage.Validator.class)
public @interface ValidQueryPage {

	String message() default "页码数必须大于0;单页容量必须大于0且小于规定最大值";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<ValidQueryPage, QueryPage> {

		@Override
		public boolean isValid(QueryPage page, ConstraintValidatorContext context) {
			if (page == null) {
				return true;
			}
			if (page.getPageNum() <= 0) {
				return false;
			}
			if (page.isPermitOutSize()) {
				if (page.getPageSize() <= 0) {
					return false;
				}
			} else {
				if (page.getPageSize() <= 0 || page.getPageSize() > DefPage.MAX_PAGE_SIZE) {
					return false;
				}
			}
			return true;
		}

	}

}
