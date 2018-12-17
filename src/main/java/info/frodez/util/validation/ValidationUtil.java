package info.frodez.util.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author Frodez
 * @date 2018-12-02
 */
public class ValidationUtil {

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	/**
	 * 对对象进行验证,如果验证通过,返回空字符串
	 * @author Frodez
	 * @param Object 需要验证的对象
	 * @date 2018-12-03
	 */
	public static String validate(Object object) {
		if(object == null) {
			return "输入不能为空!";
		}
		StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<Object> constraintViolation : validator.validate(object)) {
			sb.append(constraintViolation.getMessage());
		}
		return sb.toString();
	}
	
}
