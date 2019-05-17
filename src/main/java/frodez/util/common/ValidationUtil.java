package frodez.util.common;

import frodez.constant.settings.DefDesc;
import frodez.constant.settings.DefStr;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.experimental.UtilityClass;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

/**
 * 对象验证工具类
 * @author Frodez
 * @date 2018-12-02
 */
@UtilityClass
public class ValidationUtil {

	/**
	 * hibernate-validator错误信息配置文件位置(classpath下)
	 */
	private static final String PROPERTIESADDRESS = "others/validate-messages";

	/**
	 * 快速失败(出现第一个错误即返回)
	 */
	private static final Validator VAL = Validation.byProvider(HibernateValidator.class).configure()
		.messageInterpolator(new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator(
			PROPERTIESADDRESS))).allowOverridingMethodAlterParameterConstraint(true).failFast(true)
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
		Set<ConstraintViolation<Object>> set = VAL.forExecutables().validateParameters(instance, method, args);
		if (set.isEmpty()) {
			return null;
		}
		ConstraintViolation<Object> firstError = set.iterator().next();
		List<Node> nodes = StreamSupport.stream(firstError.getPropertyPath().spliterator(), false).filter((node) -> {
			return node.getKind() == ElementKind.CONTAINER_ELEMENT || node.getKind() == ElementKind.CROSS_PARAMETER
				|| node.getKind() == ElementKind.PARAMETER || node.getKind() == ElementKind.PROPERTY;
		}).collect(Collectors.toList());
		if (EmptyUtil.no(nodes)) {
			//获取最后一个节点,这个节点才是需要透露的信息
			return StrUtil.concat(nodes.get(nodes.size() - 1).getName(), DefStr.SEPERATOR, firstError.getMessage());
		}
		return firstError.getMessage();
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
		Set<ConstraintViolation<Object>> set = VAL.validate(object);
		if (set.isEmpty()) {
			return null;
		}
		ConstraintViolation<Object> firstError = set.iterator().next();
		//获取错误定位
		List<Node> nodes = StreamSupport.stream(firstError.getPropertyPath().spliterator(), false).filter((node) -> {
			return node.getKind() == ElementKind.CONTAINER_ELEMENT || node.getKind() == ElementKind.CROSS_PARAMETER
				|| node.getKind() == ElementKind.PARAMETER || node.getKind() == ElementKind.PROPERTY;
		}).collect(Collectors.toList());
		if (EmptyUtil.no(nodes)) {
			//获取最后一个节点,这个节点才是需要透露的信息
			return StrUtil.concat(nodes.get(nodes.size() - 1).getName(), DefStr.SEPERATOR, firstError.getMessage());
		}
		return firstError.getMessage();
	}

	/**
	 * 更改错误信息
	 * @author Frodez
	 * @date 2019-05-16
	 */
	public static ConstraintValidatorContext changeMessage(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		return context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}

}
