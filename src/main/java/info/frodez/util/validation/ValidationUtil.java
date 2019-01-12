package info.frodez.util.validation;

import java.lang.reflect.Method;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

/**
 * 对象验证工具类
 * @author Frodez
 * @date 2018-12-02
 */
public class ValidationUtil {

	private static final Validator VAL = Validation.buildDefaultValidatorFactory().getValidator();

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
		StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<Object> result : VAL.validate(object)) {
			sb.append(result.getMessage());
		}
		return sb.toString();
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
		if (args == null || args.length == 0) {
			return nullMessage;
		}
		StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<Object> result : EXEC_VAL.validateParameters(instance, method, args)) {
			sb.append(result.getMessage());
		}
		return sb.toString();
	}

}
