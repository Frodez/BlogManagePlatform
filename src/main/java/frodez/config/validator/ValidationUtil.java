package frodez.config.validator;

import frodez.constant.settings.DefDesc;
import frodez.constant.settings.DefStr;
import frodez.util.common.EmptyUtil;
import frodez.util.common.StrUtil;
import frodez.util.spring.ContextUtil;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.MessageInterpolator;
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

	@PostConstruct
	private void init() {
		ValidatorProperties properties = ContextUtil.get(ValidatorProperties.class);
		HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class).configure();
		//配置hibernate-validator消息插值源
		PlatformResourceBundleLocator locator = new PlatformResourceBundleLocator(properties.getMessageConfigPath());
		MessageInterpolator interpolator = new ResourceBundleMessageInterpolator(locator);
		configuration.allowOverridingMethodAlterParameterConstraint(true);//允许覆写接口方法约束必须为true
		configuration.failFast(true);//快速失败写死为true,能加快验证速度。如果想要修改为false,那么下面的验证方法的代码也要改变。
		engine = configuration.messageInterpolator(interpolator).buildValidatorFactory().getValidator();
	}

	/**
	 * 对方法参数进行验证,如果验证通过,返回null<br>
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
		//因为failFast写死为true,不考虑其他情况,为了提高效率,故此处直接取错误信息的第一条.
		ConstraintViolation<Object> firstError = set.iterator().next();
		List<Node> nodes = StreamSupport.stream(firstError.getPropertyPath().spliterator(), false).filter((node) -> {
			return node.getKind() == ElementKind.PROPERTY || node.getKind() == ElementKind.PARAMETER || node
				.getKind() == ElementKind.CROSS_PARAMETER;
		}).collect(Collectors.toList());
		if (EmptyUtil.no(nodes)) {
			//获取最后一个节点,这个节点才是需要透露的信息
			return StrUtil.concat(nodes.get(nodes.size() - 1).getName(), DefStr.SEPERATOR, firstError.getMessage());
		} else {
			return firstError.getMessage();
		}
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
		//因为failFast写死为true,不考虑其他情况,为了提高效率,故此处直接取错误信息的第一条.
		ConstraintViolation<Object> firstError = set.iterator().next();
		//获取错误定位
		List<Node> nodes = StreamSupport.stream(firstError.getPropertyPath().spliterator(), false).filter((node) -> {
			return node.getKind() == ElementKind.PROPERTY || node.getKind() == ElementKind.PARAMETER || node
				.getKind() == ElementKind.CROSS_PARAMETER;
		}).collect(Collectors.toList());
		if (EmptyUtil.no(nodes)) {
			//获取最后一个节点,这个节点才是需要透露的信息
			return StrUtil.concat(nodes.get(nodes.size() - 1).getName(), DefStr.SEPERATOR, firstError.getMessage());
		} else {
			return firstError.getMessage();
		}
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
