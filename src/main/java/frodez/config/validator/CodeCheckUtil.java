package frodez.config.validator;

import frodez.config.aop.validation.annotation.Check;
import frodez.util.beans.result.Result;
import frodez.util.common.StrUtil;
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
import org.springframework.beans.BeanUtils;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 代码检查相关实现
 * @author Frodez
 * @date 2019-05-22
 */
@UtilityClass
public class CodeCheckUtil {

	/**
	 * 检查字段
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static void checkField(Field field) {
		Class<?> type = field.getType();
		if (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
			checkFieldCollectionOrMap(field, (ParameterizedType) field.getGenericType());
			return;
		}
		assertComplexFieldValid(field, field.getType());
	}

	/**
	 * 检查方法参数
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static void checkParameter(Method method, Parameter parameter) {
		Class<?> type = parameter.getType();
		if (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
			checkParameterCollectionOrMap(method, parameter, (ParameterizedType) parameter.getParameterizedType());
			return;
		}
		assertComplexParameterValid(method, parameter, parameter.getType());
	}

	/**
	 * 判断是否为复杂对象
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static boolean isComplex(Class<?> type) {
		return !BeanUtils.isSimpleProperty(type);
	}

	/**
	 * 判断方法返回类型是否为ListenableFuture(泛型T为Result)
	 * @see org.springframework.util.concurrent.ListenableFuture
	 * @see frodez.util.beans.result.Result
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static boolean isAsyncResultAsReturn(Method method) {
		Type type = method.getAnnotatedReturnType().getType();
		if (!isAsyncResult(type)) {
			//async的Result放在另一处处理
			if (isResult(type)) {
				return false;
			}
			throw new IllegalArgumentException(StrUtil.concat("含有", "@", Check.class.getName(), "注解的方法", ReflectUtil
				.getFullMethodName(method), "的返回值类型必须为", ListenableFuture.class.getName(), "或者", Result.class
					.getName()));
		}
		return true;
	}

	/**
	 * 判断方法返回类型是否为Result
	 * @see org.springframework.util.concurrent.ListenableFuture
	 * @see frodez.util.beans.result.Result
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static boolean isResultAsReturn(Method method) {
		Type type = method.getAnnotatedReturnType().getType();
		if (!isResult(type)) {
			//async的Result放在另一处处理
			if (isAsyncResult(type)) {
				return false;
			}
			throw new IllegalArgumentException(StrUtil.concat("含有", "@", Check.class.getName(), "注解的方法", ReflectUtil
				.getFullMethodName(method), "的返回值类型必须为", ListenableFuture.class.getName(), "或者", Result.class
					.getName()));
		}
		return true;
	}

	private static boolean isResult(Type type) {
		if (type instanceof Class) {
			return Result.class.isAssignableFrom((Class<?>) type);
		}
		return false;
	}

	private static boolean isAsyncResult(Type type) {
		if (type instanceof ParameterizedType) {
			if (ListenableFuture.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType())) {
				Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
				if (actualTypeArgument instanceof Class && Result.class.isAssignableFrom((Class<
					?>) actualTypeArgument)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	private static void assertComplexFieldValid(Field field, Class<?> type) {
		if (isComplex(type) && field.getAnnotation(Valid.class) == null) {
			throw new RuntimeException(StrUtil.concat(field.getDeclaringClass().getName(), ".", field.getName(),
				"是复杂类型,需要加上@", Valid.class.getName(), "注解!"));
		}
	}

	private static void checkFieldCollectionOrMap(Field field, ParameterizedType genericType) {
		if (Map.class.isAssignableFrom((Class<?>) genericType.getRawType())) {
			return;
		}
		Type[] actualTypes = genericType.getActualTypeArguments();
		for (Type actualType : actualTypes) {
			//如果是直接类型,直接判断
			if (actualType instanceof Class) {
				assertComplexFieldValid(field, (Class<?>) actualType);
				continue;
			}
			//如果是泛型类型
			if (actualType instanceof ParameterizedType) {
				checkFieldCollectionOrMap(field, (ParameterizedType) actualType);
				continue;
			}
		}
	}

	private static void assertComplexParameterValid(Method method, Parameter parameter, Class<?> type) {
		if (isComplex(type) && parameter.getAnnotation(Valid.class) == null) {
			throw new IllegalArgumentException(StrUtil.concat("含有", "@", Check.class.getName(), "注解的方法", ReflectUtil
				.getFullMethodName(method), "的参数", parameter.getName(), "必须使用@", Valid.class.getName(), "注解!"));
		}
	}

	private static void checkParameterCollectionOrMap(Method method, Parameter parameter,
		ParameterizedType genericType) {
		if (Map.class.isAssignableFrom((Class<?>) genericType.getRawType())) {
			return;
		}
		Type[] actualTypes = genericType.getActualTypeArguments();
		for (Type actualType : actualTypes) {
			//如果是直接类型,直接判断
			if (actualType instanceof Class) {
				assertComplexParameterValid(method, parameter, (Class<?>) actualType);
				continue;
			}
			//如果是泛型类型
			if (actualType instanceof ParameterizedType) {
				checkParameterCollectionOrMap(method, parameter, (ParameterizedType) actualType);
				continue;
			}
		}
	}

}
