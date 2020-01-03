package frodez.util.reflect;

import frodez.constant.settings.DefStr;
import frodez.util.common.StrUtil;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
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

	private static final Map<Class<?>, Table> CGLIB_CACHE = new ConcurrentHashMap<>();

	private static final Map<String, MethodHandle> GETTER_CACHE = new ConcurrentHashMap<>();

	private static final Map<String, MethodHandle> SETTER_CACHE = new ConcurrentHashMap<>();

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
		return getFastClass(klass, false);
	}

	/**
	 * 获取FastClass,并初始化所有FastMethod。类型会被缓存。
	 * @author Frodez
	 * @date 2019-04-12
	 */
	public static FastClass getFastClass(Class<?> klass, boolean initialized) {
		Assert.notNull(klass, "klass must not be null");
		Table table = CGLIB_CACHE.get(klass);
		if (table == null) {
			table = new Table(klass);
			if (initialized) {
				table.init();
			}
			CGLIB_CACHE.put(klass, table);
			return table.fastClass;
		}
		return table.fastClass;
	}

	/**
	 * 获取method
	 * @author Frodez
	 * @date 2019-12-18
	 */
	public static Method getMethod(Class<?> klass, String method, Class<?>... params) {
		return getFastMethod(klass, method, params).getJavaMethod();
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
		Table table = CGLIB_CACHE.get(klass);
		if (table == null) {
			table = new Table(klass);
			FastMethod fastMethod = table.method(method, params);
			CGLIB_CACHE.put(klass, table);
			return fastMethod;
		}
		return table.method(method, params);
	}

	private class Table {

		FastClass fastClass;

		FastMethod[] methods;

		public Table(Class<?> klass) {
			this.fastClass = FastClass.create(klass);
			methods = new FastMethod[fastClass.getMaxIndex() + 1];
		}

		public void init() {
			for (Method method : fastClass.getClass().getMethods()) {
				int index = fastClass.getIndex(method.getName(), method.getParameterTypes());
				methods[index] = fastClass.getMethod(method);
			}
		}

		@SneakyThrows
		FastMethod method(String name, Class<?>... params) {
			int index = fastClass.getIndex(name, params);
			if (index < 0) {
				throw new NoSuchMethodException();
			}
			FastMethod method = methods[index];
			if (method == null) {
				method = fastClass.getMethod(name, params);
				methods[index] = method;
			}
			return method;
		}

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
	 * 设置目标实体的指定字段的值
	 * @param isExact value的类型是否匹配,如果匹配请选择true,速度更快
	 * @author Frodez
	 * @date 2019-12-29
	 */
	@SuppressWarnings("deprecation")
	@SneakyThrows
	public static void trySet(Class<?> klass, String fieldName, Object target, @Nullable Object value) {
		Assert.notNull(klass, "klass must not be null");
		Assert.notNull(fieldName, "fieldName must not be null");
		Assert.notNull(target, "target must not be null");
		Field field = klass.getDeclaredField(fieldName);
		if (!field.isAccessible()) {
			//暂时使用isAccessible api,因为可以减少判断次数提高性能
			field.trySetAccessible();
		}
		String identifier = StrUtil.concat(klass.getCanonicalName(), DefStr.POINT_SEPERATOR, fieldName);
		MethodHandle handle = SETTER_CACHE.get(identifier);
		if (handle == null) {
			handle = MethodHandles.lookup().unreflectSetter(field);
			SETTER_CACHE.put(identifier, handle);
		}
		handle.invoke(target, value);
	}

	/**
	 * 设置目标实体的指定字段的值
	 * @param isExact value的类型是否匹配,如果匹配请选择true,速度更快
	 * @author Frodez
	 * @date 2019-12-29
	 */
	@SuppressWarnings("deprecation")
	@SneakyThrows
	public static void trySet(Field field, Object target, @Nullable Object value) {
		Assert.notNull(field, "field must not be null");
		Assert.notNull(target, "target must not be null");
		if (!field.isAccessible()) {
			//暂时使用isAccessible api,因为可以减少判断次数提高性能
			field.trySetAccessible();
		}
		String identifier = StrUtil.concat(field.getDeclaringClass().getCanonicalName(), DefStr.POINT_SEPERATOR, field.getName());
		MethodHandle handle = SETTER_CACHE.get(identifier);
		if (handle == null) {
			handle = MethodHandles.lookup().unreflectGetter(field);
			GETTER_CACHE.put(identifier, handle);
		}
		handle.invoke(target, value);
	}

	/**
	 * 获取目标实体的指定字段
	 * @author Frodez
	 * @date 2019-12-29
	 */
	@SuppressWarnings("deprecation")
	@SneakyThrows
	public static Object tryGet(Class<?> klass, String fieldName, Object target) {
		Assert.notNull(klass, "klass must not be null");
		Assert.notNull(fieldName, "fieldName must not be null");
		Assert.notNull(target, "target must not be null");
		Field field = klass.getDeclaredField(fieldName);
		if (!field.isAccessible()) {
			//暂时使用isAccessible api,因为可以减少判断次数提高性能
			field.trySetAccessible();
		}
		String identifier = StrUtil.concat(klass.getCanonicalName(), DefStr.POINT_SEPERATOR, fieldName);
		MethodHandle handle = GETTER_CACHE.get(identifier);
		if (handle == null) {
			handle = MethodHandles.lookup().unreflectGetter(field);
			GETTER_CACHE.put(identifier, handle);
		}
		return handle.invoke(target);
	}

	/**
	 * 获取目标实体的指定字段
	 * @author Frodez
	 * @date 2019-12-29
	 */
	@SuppressWarnings("deprecation")
	@SneakyThrows
	public static Object tryGet(Field field, Object target) {
		Assert.notNull(field, "field must not be null");
		Assert.notNull(target, "target must not be null");
		if (!field.isAccessible()) {
			//暂时使用isAccessible api,因为可以减少判断次数提高性能
			field.trySetAccessible();
		}
		String identifier = StrUtil.concat(field.getDeclaringClass().getCanonicalName(), DefStr.POINT_SEPERATOR, field.getName());
		MethodHandle handle = GETTER_CACHE.get(identifier);
		if (handle == null) {
			handle = MethodHandles.lookup().unreflectGetter(field);
			GETTER_CACHE.put(identifier, handle);
		}
		return handle.invoke(target);
	}

}
