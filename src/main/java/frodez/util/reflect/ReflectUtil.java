package frodez.util.reflect;

import frodez.constant.settings.DefStr;
import frodez.util.beans.pair.Pair;
import frodez.util.common.StrUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 反射工具类<br>
 * 警告:对需要动态修改类的情况不适用。<br>
 * @author Frodez
 * @date 2019-01-13
 */
@UtilityClass
public class ReflectUtil {

	public static final Object[] EMPTY_ARRAY = new Object[] { null };

	private static final Map<Class<?>, Pair<FastClass, FastMethod[]>> CGLIB_CACHE = new ConcurrentHashMap<>();

	/**
	 * 对象实例化
	 * @author Frodez
	 * @date 2019-06-19
	 */
	@SneakyThrows
	@SuppressWarnings("unchecked")
	public static <T> T instance(Class<T> klass) {
		return (T) getFastClass(klass).newInstance();
	}

	/**
	 * 对象实例化
	 * @author Frodez
	 * @date 2019-06-19
	 */
	@SneakyThrows
	public static <T> Supplier<T> supplier(Class<T> klass) {
		Assert.notNull(klass, "klass must not be null");
		return () -> instance(klass);
	}

	/**
	 * 获取FastClass,类型会被缓存
	 * @author Frodez
	 * @date 2019-04-12
	 */
	public static FastClass getFastClass(Class<?> klass) {
		Assert.notNull(klass, "klass must not be null");
		Pair<FastClass, FastMethod[]> pair = CGLIB_CACHE.get(klass);
		if (pair == null) {
			FastClass fastClass = FastClass.create(klass);
			FastMethod[] methods = new FastMethod[fastClass.getMaxIndex() + 1];
			pair = new Pair<>();
			pair.setKey(fastClass);
			pair.setValue(methods);
			CGLIB_CACHE.put(klass, pair);
			return fastClass;
		}
		return pair.getKey();
	}

	/**
	 * 获取FastMethod,方法会被缓存
	 * @author Frodez
	 * @throws NoSuchMethodException
	 * @date 2019-04-12
	 */
	@SneakyThrows
	public static FastMethod getFastMethod(Class<?> klass, String method, Class<?>... params) {
		Assert.notNull(klass, "klass must not be null");
		Assert.notNull(method, "method must not be null");
		Pair<FastClass, FastMethod[]> pair = CGLIB_CACHE.get(klass);
		if (pair == null) {
			FastClass fastClass = FastClass.create(klass);
			FastMethod[] methods = new FastMethod[fastClass.getMaxIndex() + 1];
			int index = fastClass.getIndex(method, params);
			if (index < 0) {
				throw new NoSuchMethodException();
			}
			FastMethod fastMethod = fastClass.getMethod(method, params);
			methods[fastMethod.getIndex()] = fastMethod;
			pair = new Pair<>();
			pair.setKey(fastClass);
			pair.setValue(methods);
			CGLIB_CACHE.put(klass, pair);
			return fastMethod;
		}
		FastClass fastClass = pair.getKey();
		int index = fastClass.getIndex(method, params);
		if (index < 0) {
			throw new NoSuchMethodException();
		}
		FastMethod[] methods = pair.getValue();
		FastMethod fastMethod = methods[index];
		if (fastMethod == null) {
			fastMethod = fastClass.getMethod(method, params);
			methods[index] = fastMethod;
		}
		return fastMethod;
	}

	/**
	 * 获取方法全限定名
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getFullMethodName(Method method) {
		return StrUtil.concat(method.getDeclaringClass().getName(), DefStr.POINT_SEPERATOR, method.getName());
	}

	/**
	 * 获取方法全限定名
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getShortMethodName(Method method) {
		return StrUtil.concat(method.getDeclaringClass().getSimpleName(), DefStr.POINT_SEPERATOR, method.getName());
	}

	/**
	 * 获取字段全限定名
	 * @author Frodez
	 * @date 2019-06-05
	 */
	public static String getFullFieldName(Field field) {
		return StrUtil.concat(field.getDeclaringClass().getName(), DefStr.POINT_SEPERATOR, field.getName());
	}

	/**
	 * 获取字段全限定名
	 * @author Frodez
	 * @date 2019-06-05
	 */
	public static String getShortFieldName(Field field) {
		return StrUtil.concat(field.getDeclaringClass().getSimpleName(), DefStr.POINT_SEPERATOR, field.getName());
	}

	/**
	 * 给类的字段赋值,赋值后还原accessible设置
	 * @author Frodez
	 * @date 2019-12-08
	 */
	public static void trySet(Class<?> klass, String fieldName, @Nullable Object target, Object value) {
		trySet(klass, fieldName, target, value, true);
	}

	/**
	 * 给类的字段赋值
	 * @param reviveAccessible 赋值后是否还原accessible设置
	 * @author Frodez
	 * @date 2019-12-08
	 */
	@SneakyThrows
	public static void trySet(Class<?> klass, String fieldName, Object target, @Nullable Object value, boolean reviveAccessible) {
		Assert.notNull(klass, "klass must not be null");
		Assert.notNull(fieldName, "fieldName must not be null");
		Assert.notNull(target, "target must not be null");
		Field field = klass.getDeclaredField(fieldName);
		if (reviveAccessible) {
			boolean accessible = field.canAccess(target);
			field.setAccessible(true);
			field.set(target, value);
			field.setAccessible(accessible);
		} else {
			field.setAccessible(true);
			field.set(target, value);
		}
	}

	/**
	 * 给类的字段赋值,赋值后还原accessible设置
	 * @author Frodez
	 * @date 2019-12-08
	 */
	public static void trySet(Field field, Object target, @Nullable Object value) {
		trySet(field, target, value, true);
	}

	/**
	 * 给类的字段赋值
	 * @param reviveAccessible 赋值后是否还原accessible设置
	 * @author Frodez
	 * @date 2019-12-08
	 */
	@SneakyThrows
	public static void trySet(Field field, Object target, @Nullable Object value, boolean reviveAccessible) {
		Assert.notNull(field, "field must not be null");
		Assert.notNull(target, "target must not be null");
		if (reviveAccessible) {
			boolean accessible = field.canAccess(target);
			field.setAccessible(true);
			field.set(target, value);
			field.setAccessible(accessible);
		} else {
			field.setAccessible(true);
			field.set(target, value);
		}
	}

	/**
	 * 获取字段的值,取值后还原accessible设置
	 * @author Frodez
	 * @date 2019-12-08
	 */
	public static Object tryGet(Class<?> klass, String fieldName, Object target) {
		return tryGet(klass, fieldName, target, true);
	}

	/**
	 * 获取字段的值
	 * @param reviveAccessible 取值后是否还原accessible设置
	 * @author Frodez
	 * @date 2019-12-08
	 */
	@SneakyThrows
	public static Object tryGet(Class<?> klass, String fieldName, Object target, boolean reviveAccessible) {
		Assert.notNull(klass, "klass must not be null");
		Assert.notNull(fieldName, "fieldName must not be null");
		Assert.notNull(target, "target must not be null");
		Field field = klass.getDeclaredField(fieldName);
		if (reviveAccessible) {
			boolean accessible = field.canAccess(target);
			field.setAccessible(true);
			Object result = field.get(target);
			field.setAccessible(accessible);
			return result;
		} else {
			field.setAccessible(true);
			return field.get(target);
		}
	}

	/**
	 * 获取字段的值,取值后还原accessible设置
	 * @author Frodez
	 * @date 2019-12-08
	 */
	public static Object tryGet(Field field, Object target) {
		return tryGet(field, target, true);
	}

	/**
	 * 获取字段的值
	 * @param reviveAccessible 取值后是否还原accessible设置
	 * @author Frodez
	 * @date 2019-12-08
	 */
	@SneakyThrows
	public static Object tryGet(Field field, Object target, boolean reviveAccessible) {
		Assert.notNull(field, "field must not be null");
		Assert.notNull(target, "target must not be null");
		if (reviveAccessible) {
			boolean accessible = field.canAccess(target);
			field.setAccessible(true);
			Object result = field.get(target);
			field.setAccessible(accessible);
			return result;
		} else {
			field.setAccessible(true);
			return field.get(target);
		}
	}

}
