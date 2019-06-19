package frodez.config.aop.util;

import frodez.config.aop.validation.annotation.Check;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.beans.result.Result;
import frodez.util.common.EmptyUtil;
import frodez.util.reflect.ReflectUtil;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * AOP工具类
 * @author Frodez
 * @date 2019-05-23
 */
@UtilityClass
public class AOPUtil {

	/**
	 * 判断方法返回类型是否为AsyncResult-即ListenableFuture(类型参数T为Result)<br>
	 * 如果是AsyncResult,返回true.如果是Result,返回false.<br>
	 * 如果既不是AsyncResult,也不是Result,则抛出异常.<br>
	 * @see org.springframework.util.concurrent.ListenableFuture
	 * @see frodez.util.beans.result.Result
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static boolean isAsyncResultAsReturn(Method method) throws CodeCheckException {
		Assert.notNull(method, "method must not be null");
		Type type = method.getAnnotatedReturnType().getType();
		if (isAsyncResult(type)) {
			return true;
		}
		if (isResult(type)) {
			return false;
		}
		throw new CodeCheckException("含有", "@", Check.class.getCanonicalName(), "注解的方法", ReflectUtil.getFullMethodName(
			method), "的返回值类型必须为", ListenableFuture.class.getCanonicalName(), "或者", Result.class.getCanonicalName());
	}

	/**
	 * 判断方法返回类型是否为Result<br>
	 * 如果是Result,返回true.如果是AsyncResult,返回false.<br>
	 * 如果既不是AsyncResult-即ListenableFuture(类型参数T为Result),也不是Result,则抛出异常.<br>
	 * @see org.springframework.util.concurrent.ListenableFuture
	 * @see frodez.util.beans.result.Result
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static boolean isResultAsReturn(Method method) throws CodeCheckException {
		Assert.notNull(method, "method must not be null");
		Type type = method.getAnnotatedReturnType().getType();
		if (isResult(type)) {
			return true;
		}
		if (isAsyncResult(type)) {
			return false;
		}
		throw new CodeCheckException("含有", "@", Check.class.getCanonicalName(), "注解的方法", ReflectUtil.getFullMethodName(
			method), "的返回值类型必须为", ListenableFuture.class.getCanonicalName(), "或者", Result.class.getCanonicalName());
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
