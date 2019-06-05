package frodez.config.code.rule.hibernate;

import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.config.code.rule.CodeCheckRule;
import frodez.constant.annotations.decoration.EnumCheckable;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.common.StrUtil;
import frodez.util.reflect.ReflectUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

@Slf4j
public class LegalEnumRule implements CodeCheckRule {

	private Set<Class<?>> legalEnumCheckCache = new HashSet<>();

	@Override
	public void check(Field field) throws CodeCheckException {
		LegalEnum annotation = field.getAnnotation(LegalEnum.class);
		if (annotation != null) {
			if (!ClassUtils.isPrimitiveOrWrapper(field.getType())) {
				throw new CodeCheckException(ReflectUtil.getFullFieldName(field), "不是基本类型或者其装箱类,不能使用@", LegalEnum.class
					.getName(), "注解");
			}
			checkLegalEnum(annotation);
		}
	}

	@Override
	public void check(Method method) throws CodeCheckException {
		for (Parameter parameter : method.getParameters()) {
			LegalEnum annotation = parameter.getAnnotation(LegalEnum.class);
			if (annotation != null) {
				if (!ClassUtils.isPrimitiveOrWrapper(parameter.getType())) {
					throw new CodeCheckException("方法", ReflectUtil.getFullMethodName(method), "的参数", parameter
						.getName(), "不是基本类型或者其装箱类,不能使用@", LegalEnum.class.getName(), "注解");
				}
				checkLegalEnum(annotation);
			}
		}
	}

	private void checkLegalEnum(LegalEnum annotation) {
		try {
			Class<?> enumClass = annotation.type();
			if (legalEnumCheckCache.contains(enumClass)) {
				return;
			}
			String enumClassName = enumClass.getName();
			if (!enumClass.isAnnotationPresent(EnumCheckable.class)) {
				log.warn(StrUtil.concat(enumClassName, "被用于枚举类型检查,但是没有@", EnumCheckable.class.getName(), "注解"));
			}
			String methodName = annotation.method();
			Class<?> parameterType = annotation.paramType();
			Method method;
			try {
				method = enumClass.getMethod(methodName, parameterType);
			} catch (NoSuchMethodException e) {
				throw new CodeCheckException(enumClassName, "必须拥有", methodName, "方法,详情参见@", EnumCheckable.class
					.getName(), "注解");
			}
			if (method.getReturnType() != enumClass) {
				throw new CodeCheckException(enumClassName, "的", methodName, "方法返回值必须为", enumClassName, ",详情参见@",
					EnumCheckable.class.getName(), "注解");
			}
			String valuesMethodName = annotation.valuesMethod();
			try {
				enumClass.getMethod(valuesMethodName);
			} catch (NoSuchMethodException e) {
				throw new CodeCheckException(enumClassName, "必须拥有", valuesMethodName, "方法,详情参见@", EnumCheckable.class
					.getName(), "注解");
			}
			synchronized (legalEnumCheckCache) {
				legalEnumCheckCache.add(enumClass);
			}
		} catch (SecurityException e) {
			throw new CodeCheckException(e);
		}
	}

}
