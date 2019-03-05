package frodez.util.common;

import java.lang.reflect.Method;
import java.util.Iterator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import org.hibernate.validator.HibernateValidator;

/**
 * 对象验证工具类
 * @author Frodez
 * @date 2018-12-02
 */
public class ValidationUtil {

	/**
	 * 快速失败(出现第一个错误即返回)
	 */
	private static final Validator VAL = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
		.buildValidatorFactory().getValidator();

	private static final ExecutableValidator EXEC_VAL = VAL.forExecutables();

	/**
	 * 空对象的默认返回值
	 */
	private static final String DEFAULT_NULL_MESSAGE = "输入不能为空!";

	/**
	 * 对对象进行验证,如果验证通过,返回空字符串<br>
	 * 空对象默认返回值参见ValidationUtil.DEFAULT_NULL_MESSAGE
	 * @author Frodez
	 * @param Object 需要验证的对象
	 * @date 2018-12-03
	 */
	public static String validate(Object object) {
		return validate(object, DEFAULT_NULL_MESSAGE);
	}

	/**
	 * 对对象进行验证,如果验证通过,返回空字符串
	 * @author Frodez
	 * @param Object 需要验证的对象
	 * @param nullMessage 验证对象为空时返回的字符串
	 * @date 2018-12-03
	 */
	public static String validate(Object object, String nullMessage) {
		if (object == null) {
			return nullMessage;
		}
		Iterator<ConstraintViolation<Object>> iterator = VAL.validate(object).iterator();
		return iterator.hasNext() ? iterator.next().getMessage() : null;
	}

	/**
	 * 对方法参数进行验证,如果验证通过,返回空字符串<br>
	 * 空方法参数默认返回值参见ValidationUtil.DEFAULT_NULL_MESSAGE
	 * @author Frodez
	 * @param instance 需要验证的方法所在类实例
	 * @param method 需要验证的方法
	 * @param args 方法参数
	 * @date 2019-01-12
	 */
	public static String validateParam(Object instance, Method method, Object[] args) {
		return validateParam(instance, method, args, DEFAULT_NULL_MESSAGE);
	}

	/**
	 * 对方法参数进行验证,如果验证通过,返回空字符串<br>
	 * @author Frodez
	 * @param instance 需要验证的方法所在类实例
	 * @param method 需要验证的方法
	 * @param args 方法参数
	 * @param nullMessage 验证方法参数为空时返回的字符串
	 * @date 2019-01-12
	 */
	public static String validateParam(Object instance, Method method, Object[] args, String nullMessage) {
		if (instance == null) {
			return nullMessage;
		}
		Iterator<ConstraintViolation<Object>> iterator = EXEC_VAL.validateParameters(instance, method, args).iterator();
		return iterator.hasNext() ? iterator.next().getMessage() : null;
	}

}
