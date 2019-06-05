package frodez.config.validator;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.constant.annotations.decoration.EnumCheckable;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.common.StrUtil;
import frodez.util.reflect.BeanUtil;
import frodez.util.reflect.ReflectUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import javax.validation.Valid;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * hibernate-validator代码检查相关实现
 * @author Frodez
 * @date 2019-05-22
 */
@Slf4j
@UtilityClass
public class CodeCheckUtil {

	/**
	 * 检查实体类
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static void checkClass(Class<?> klass) throws CodeCheckException {
		Assert.notNull(klass, "field must not be null");
		for (Field field : BeanUtil.getSetterFields(klass)) {
			checkField(field);
		}
	}

	/**
	 * 检查字段
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static void checkField(Field field) throws CodeCheckException {
		Assert.notNull(field, "field must not be null");
		Class<?> type = field.getType();
		if (!Collection.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type)) {
			assertFieldValid(field, field.getType());
		} else {
			checkFieldCollectionOrMap(field, (ParameterizedType) field.getGenericType());
		}
		checkLegalEnum(field);
	}

	/**
	 * 检查方法参数
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static void checkParameter(Method method, Parameter parameter) throws CodeCheckException {
		Assert.notNull(method, "method must not be null");
		Assert.notNull(parameter, "parameter must not be null");
		if (method.getParameterCount() == 0) {
			throw new CodeCheckException(StrUtil.concat("@", Check.class.getName(), "注解不能在无参数的方法", ReflectUtil
				.getFullMethodName(method), "上使用"));
		}
		Class<?> type = parameter.getType();
		if (!Collection.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type)) {
			assertParameterValid(method, parameter, parameter.getType());
		} else {
			checkParameterCollectionOrMap(method, parameter, (ParameterizedType) parameter.getParameterizedType());
		}
		checkLegalEnum(parameter);
	}

	private static void assertFieldValid(Field field, Class<?> type) {
		if (field.getAnnotation(ValidateBean.class) != null && field.getAnnotation(Valid.class) == null) {
			throw new CodeCheckException(StrUtil.concat(field.getDeclaringClass().getName(), ".", field.getName(),
				"是复杂类型,需要加上@", Valid.class.getName(), "注解!"));
		}
	}

	private static void checkFieldCollectionOrMap(Field field, ParameterizedType genericType) {
		if (Map.class.isAssignableFrom((Class<?>) genericType.getRawType())) {
			return;
		}
		for (Type actualType : genericType.getActualTypeArguments()) {
			//如果是直接类型,直接判断
			if (actualType instanceof Class) {
				assertFieldValid(field, (Class<?>) actualType);
				continue;
			}
			//如果是泛型类型
			if (actualType instanceof ParameterizedType) {
				checkFieldCollectionOrMap(field, (ParameterizedType) actualType);
				continue;
			}
		}
	}

	private static void assertParameterValid(Method method, Parameter parameter, Class<?> type) {
		if (parameter.getAnnotation(ValidateBean.class) != null && parameter.getAnnotation(Valid.class) == null) {
			throw new CodeCheckException(StrUtil.concat("含有", "@", Check.class.getName(), "注解的方法", ReflectUtil
				.getFullMethodName(method), "的参数", parameter.getName(), "必须使用@", Valid.class.getName(), "注解!"));
		}
	}

	private static void checkParameterCollectionOrMap(Method method, Parameter parameter,
		ParameterizedType genericType) {
		if (Map.class.isAssignableFrom((Class<?>) genericType.getRawType())) {
			return;
		}
		for (Type actualType : genericType.getActualTypeArguments()) {
			//如果是直接类型,直接判断
			if (actualType instanceof Class) {
				assertParameterValid(method, parameter, (Class<?>) actualType);
				continue;
			}
			//如果是泛型类型
			if (actualType instanceof ParameterizedType) {
				checkParameterCollectionOrMap(method, parameter, (ParameterizedType) actualType);
				continue;
			}
		}
	}

	private static void checkLegalEnum(Field field) {
		checkLegalEnum(field.getAnnotation(LegalEnum.class));
	}

	private static void checkLegalEnum(Parameter parameter) {
		checkLegalEnum(parameter.getAnnotation(LegalEnum.class));
	}

	private static void checkLegalEnum(LegalEnum annotation) {
		try {
			if (annotation != null) {
				Class<?> enumClass = annotation.type();
				String enumClassName = enumClass.getName();
				if (!enumClass.isAnnotationPresent(EnumCheckable.class)) {
					log.warn(StrUtil.concat(enumClassName, "被用于枚举类型检查,但是没有@", EnumCheckable.class.getName(), "注解"));
				}
				String methodName = annotation.method();
				Class<?> parameterType = annotation.paramType();
				Method method = enumClass.getMethod(methodName, parameterType);
				if (method == null) {
					throw new CodeCheckException(StrUtil.concat(enumClassName, "必须拥有", methodName, "方法,详情参见@",
						EnumCheckable.class.getName(), "注解"));
				}
				if (method.getReturnType() != enumClass) {
					throw new CodeCheckException(StrUtil.concat(enumClassName, "的", methodName, "方法返回值必须为",
						enumClassName, ",详情参见@", EnumCheckable.class.getName(), "注解"));
				}
				String valuesMethodName = annotation.valuesMethod();
				Method values = enumClass.getMethod(valuesMethodName);
				if (values == null) {
					throw new CodeCheckException(StrUtil.concat(enumClassName, "必须拥有", valuesMethodName, "方法,详情参见@",
						EnumCheckable.class.getName(), "注解"));
				}
			}
		} catch (Exception e) {
			if (!(e instanceof CodeCheckException)) {
				throw new CodeCheckException(e.getMessage());
			}
		}
	}

}
