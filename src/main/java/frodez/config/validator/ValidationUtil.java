package frodez.config.validator;

import frodez.constant.settings.DefDesc;
import frodez.constant.settings.DefStr;
import frodez.util.common.StrUtil;
import frodez.util.spring.ContextUtil;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * 对象验证工具类
 * @author Frodez
 * @date 2018-12-02
 */
@Component
@DependsOn("contextUtil")
public class ValidationUtil {

	/**
	 * 验证引擎
	 */
	private static Validator engine;

	/**
	 * 快速失败配置
	 */
	private static boolean failFast;

	@PostConstruct
	private void init() {
		ValidatorProperties properties = ContextUtil.bean(ValidatorProperties.class);
		HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class).configure();
		//配置hibernate-validator消息插值源
		MessageInterpolator interpolator = new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator(properties
			.getMessageConfigPath()));
		configuration.messageInterpolator(interpolator);
		//配置快速失败
		configuration.failFast(properties.getFailFast());
		failFast = properties.getFailFast();
		engine = configuration.buildValidatorFactory().getValidator();
	}

	/**
	 * 更改错误信息
	 * @author Frodez
	 * @date 2019-05-16
	 */
	public static void changeMessage(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}

	/**
	 * 对方法参数进行验证,如果验证通过,返回null<br>
	 * <strong>用于AOP,因为AOP做出了保证,故无NPE检查。如需要单独使用,请保证参数均非null。</strong>
	 * @author Frodez
	 * @param instance 需要验证的方法所在类实例
	 * @param method 需要验证的方法
	 * @param args 方法参数
	 * @date 2019-01-12
	 */
	public static String validateParam(final Object instance, final Method method, final Object[] args) {
		Set<ConstraintViolation<Object>> set = engine.forExecutables().validateParameters(instance, method, args);
		if (set.isEmpty()) {
			return null;
		}
		return failFast ? getErrorMessage(set.iterator().next()) : getErrorMessage(set);
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
	 * 对对象进行验证,如果验证通过,返回null
	 * @author Frodez
	 * @param Object 需要验证的对象
	 * @param nullMessage 验证对象为空时返回的字符串
	 * @date 2018-12-03
	 */
	public static String validate(final Object object, String nullMessage) {
		if (object == null) {
			return nullMessage;
		}
		Set<ConstraintViolation<Object>> set = engine.validate(object);
		if (set.isEmpty()) {
			return null;
		}
		return failFast ? getErrorMessage(set.iterator().next()) : getErrorMessage(set);
	}

	/**
	 * 获取格式化的错误信息
	 * @author Frodez
	 * @date 2019-11-27
	 */
	private static String getErrorMessage(Set<ConstraintViolation<Object>> violations) {
		List<String> messages = violations.stream().map(ValidationUtil::getErrorMessage).filter((message) -> message != null).collect(Collectors
			.toList());
		return String.join(";\n", messages);
	}

	/**
	 * 获取格式化的错误信息
	 * @author Frodez
	 * @date 2019-06-11
	 */
	private static String getErrorMessage(ConstraintViolation<Object> violation) {
		String errorSource = getErrorSource(violation);
		return errorSource == null ? violation.getMessage() : StrUtil.concat(errorSource, DefStr.SEPERATOR, violation.getMessage());
	}

	/**
	 * 获取错误信息源
	 * @author Frodez
	 * @date 2019-06-11
	 */
	private static String getErrorSource(ConstraintViolation<Object> violation) {
		Stream<Node> stream = StreamSupport.stream(violation.getPropertyPath().spliterator(), false);
		List<String> nodes = stream.filter(isErrorSouce).map(Path.Node::toString).collect(Collectors.toList());
		return nodes.isEmpty() ? null : String.join(DefStr.POINT_SEPERATOR, nodes);
	}

	/**
	 * 判断是否为所需的错误信息节点
	 */
	private static Predicate<Node> isErrorSouce = (node) -> {
		switch (node.getKind()) {
			case PROPERTY :
				return true;
			case PARAMETER :
				return true;
			case CROSS_PARAMETER :
				return true;
			case CONTAINER_ELEMENT :
				return true;
			default :
				return false;
		}
	};

}
