package frodez.config.validator;

import frodez.config.aop.validation.annotation.Check;
import frodez.util.beans.result.Result;
import frodez.util.common.EmptyUtil;
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
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * hibernate-validator代码检查相关实现
 * @author Frodez
 * @date 2019-05-22
 */
@UtilityClass
public class CodeCheckUtil {

	/**
	 * 检查实体类
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static void checkClass(Class<?> klass) {
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
	public static void checkField(Field field) {
		Assert.notNull(field, "field must not be null");
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
		Assert.notNull(method, "method must not be null");
		Assert.notNull(parameter, "parameter must not be null");
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
		Assert.notNull(type, "type must not be null");
		return !BeanUtils.isSimpleProperty(type);
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
		for (Type actualType : genericType.getActualTypeArguments()) {
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
			throw new RuntimeException(StrUtil.concat("含有", "@", Check.class.getName(), "注解的方法", ReflectUtil
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

	/**
	 * 判断方法返回类型是否为AsyncResult-即ListenableFuture(类型参数T为Result)<br>
	 * 如果既不是AsyncResult,也不是Result,则抛出异常.<br>
	 * @see org.springframework.util.concurrent.ListenableFuture
	 * @see frodez.util.beans.result.Result
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static boolean isAsyncResultAsReturn(Method method) {
		Assert.notNull(method, "method must not be null");
		Type type = method.getAnnotatedReturnType().getType();
		if (isAsyncResult(type)) {
			return true;
		}
		if (isResult(type)) {
			return false;
		}
		throw new RuntimeException(StrUtil.concat("含有", "@", Check.class.getName(), "注解的方法", ReflectUtil
			.getFullMethodName(method), "的返回值类型必须为", ListenableFuture.class.getName(), "或者", Result.class.getName()));
	}

	/**
	 * 判断方法返回类型是否为Result<br>
	 * 如果既不是AsyncResult-即ListenableFuture(类型参数T为Result),也不是Result,则抛出异常.<br>
	 * @see org.springframework.util.concurrent.ListenableFuture
	 * @see frodez.util.beans.result.Result
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static boolean isResultAsReturn(Method method) {
		Assert.notNull(method, "method must not be null");
		Type type = method.getAnnotatedReturnType().getType();
		if (isResult(type)) {
			return true;
		}
		if (isAsyncResult(type)) {
			return false;
		}
		throw new RuntimeException(StrUtil.concat("含有", "@", Check.class.getName(), "注解的方法", ReflectUtil
			.getFullMethodName(method), "的返回值类型必须为", ListenableFuture.class.getName(), "或者", Result.class.getName()));
	}

	private static boolean isResult(Type type) {
		return type instanceof Class ? Result.class.isAssignableFrom((Class<?>) type) : false;
	}

	private static boolean isAsyncResult(Type type) {
		if (!(type instanceof ParameterizedType)) {
			return false;
		}
		Type rawType = ((ParameterizedType) type).getRawType();
		if (!(rawType instanceof Class) || !ListenableFuture.class.isAssignableFrom((Class<?>) rawType)) {
			return false;
		}
		Type[] types = ((ParameterizedType) type).getActualTypeArguments();
		if (EmptyUtil.yes(types)) {
			return false;
		}
		Type typeArgument = types[0];
		return typeArgument instanceof Class && Result.class.isAssignableFrom((Class<?>) typeArgument);
	}

}
