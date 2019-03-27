package frodez.util.common;

import frodez.util.constant.setting.DefDesc;
import java.lang.reflect.Method;
import java.util.Iterator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.experimental.UtilityClass;
import org.hibernate.validator.HibernateValidator;

/**
 * 对象验证工具类
 * @author Frodez
 * @date 2018-12-02
 */
@UtilityClass
public class ValidationUtil {

	/**
	 * 快速失败(出现第一个错误即返回)
	 */
	private static final Validator VAL = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
		.buildValidatorFactory().getValidator();

	/**
	 * 对方法参数进行验证,如果验证通过,返回null<br>
	 * @author Frodez
	 * @param instance 需要验证的方法所在类实例
	 * @param method 需要验证的方法
	 * @param args 方法参数
	 * @date 2019-01-12
	 */
	public static String validateParam(final Object instance, final Method method, final Object[] args) {
		Iterator<ConstraintViolation<Object>> iterator = VAL.forExecutables().validateParameters(instance, method, args)
			.iterator();
		return iterator.hasNext() ? iterator.next().getMessage() : null;
	}

	/**
	 * 对对象进行验证,如果验证通过,返回null<br>
	 * 空对象默认返回值参见DefDesc.Warn.NULL_WARN
	 * @author Frodez
	 * @param Object 需要验证的对象
	 * @date 2018-12-03
	 */
	public static String validate(final Object object) {
		return validate(object, DefDesc.Warn.NULL_WARN);
	}

	/**
	 * 对对象进行验证,如果验证通过,返回空字符串
	 * @author Frodez
	 * @param Object 需要验证的对象
	 * @param nullMessage 验证对象为空时返回的字符串
	 * @date 2018-12-03
	 */
	public static String validate(final Object object, String nullMessage) {
		if (object == null) {
			return nullMessage;
		}
		Iterator<ConstraintViolation<Object>> iterator = VAL.validate(object).iterator();
		return iterator.hasNext() ? iterator.next().getMessage() : null;
	}

}
