package frodez.config.aop.validation.annotation.special;

import frodez.config.validator.ValidationUtil;
import frodez.constant.settings.DefPage;
import frodez.util.beans.param.QueryPage;
import frodez.util.common.StrUtil;
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
 * QueryPage专用校验<br>
 * <strong>警告:仅用于QueryPage!</strong>
 * @see frodez.util.beans.param.QueryPage
 * @author Frodez
 * @date 2019-05-16
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidQueryPage.Validator.class)
public @interface ValidQueryPage {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<ValidQueryPage, QueryPage> {

		/**
		 * 缓存字符串,避免每次重复toString()带来额外开销
		 */
		private static String MAX_PAGE_SIZE = Integer.valueOf(DefPage.MAX_PAGE_SIZE).toString();

		@Override
		public boolean isValid(QueryPage page, ConstraintValidatorContext context) {
			if (page == null) {
				return true;
			}
			if (page.getPageNum() <= 0) {
				ValidationUtil.changeMessage(context, StrUtil.concat("pageNum的值为", page.getPageNum().toString(),
					",但要求是正数."));
				return false;
			}
			if (page.isPermitOutSize()) {
				if (page.getPageSize() <= 0) {
					ValidationUtil.changeMessage(context, StrUtil.concat("pageSize的值为", page.getPageSize().toString(),
						"但要求是正数."));
					return false;
				}
			} else {
				if (page.getPageSize() <= 0 || page.getPageSize() > DefPage.MAX_PAGE_SIZE) {
					ValidationUtil.changeMessage(context, StrUtil.concat("pageSize的值为", page.getPageSize().toString(),
						",但要求是正数且不大于", MAX_PAGE_SIZE, "."));
					return false;
				}
			}
			return true;
		}

	}

}
