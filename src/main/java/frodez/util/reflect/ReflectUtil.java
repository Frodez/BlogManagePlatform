package frodez.util.reflect;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.Assert;

/**
 * 反射工具类
 * @author Frodez
 * @date 2019-01-13
 */
public class ReflectUtil {

	/**
	 * 方法缓存
	 */
	private static final Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();

	/**
	 * 根据方法名获取方法,不支持重载方法<br>
	 * 重载方法会返回方法列表中的第一个<br>
	 * @author Frodez
	 * @date 2018-12-17
	 */
	public static Method getMethod(Class<?> klass, String methodName) throws RuntimeException {
		Assert.notNull(klass, "类型不能为空!");
		Assert.notNull(methodName, "方法名不能为空!");
		String fullName = klass.getName() + "." + methodName;
		Method method = METHOD_CACHE.get(fullName);
		if (method != null) {
			return method;
		}
		Method[] methods = klass.getMethods();
		for (Method m : methods) {
			if (methodName.equals(m.getName())) {
				METHOD_CACHE.put(fullName, m);
				return m;
			}
		}
		throw new RuntimeException("方法不存在!");
	}

	/**
	 * 获取方法全限定名
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getFullMethodName(Method method) {
		Assert.notNull(method, "方法不能为空!");
		return method.getDeclaringClass().getName() + "." + method.getName();
	}

	/**
	 * 获取方法全限定名
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getShortMethodName(Method method) {
		Assert.notNull(method, "方法不能为空!");
		return method.getDeclaringClass().getSimpleName() + "." + method.getName();
	}

	/**
	 * 基本数据类型转换<br>
	 * 涉及类型:byte, short, int, long以及对应装箱类
	 * @author Frodez
	 * @date 2018-12-17
	 */
	public static Object castValue(Object value, Class<?> parameterClass) {
		if (value == null) {
			return null;
		}
		Assert.notNull(parameterClass, "目标类型不能为空!");
		Class<?> valueClass = value.getClass();
		if (valueClass == byte.class || valueClass == Byte.class) {
			return castByteValue(parameterClass, (Byte) value);
		}
		if (valueClass == short.class || valueClass == Short.class) {
			return castShortValue(parameterClass, (Short) value);
		}
		if (valueClass == int.class || valueClass == Integer.class) {
			return castIntValue(parameterClass, (Integer) value);
		}
		if (valueClass == long.class || valueClass == Long.class) {
			return castLongValue(parameterClass, (Long) value);
		}
		return value;
	}

	private static Object castByteValue(Class<?> parameterClass, Byte value) {
		if (parameterClass == byte.class || parameterClass == Byte.class) {
			return value;
		}
		if (parameterClass == short.class || parameterClass == Short.class) {
			return value.shortValue();
		}
		if (parameterClass == int.class || parameterClass == Integer.class) {
			return value.intValue();
		}
		if (parameterClass == long.class || parameterClass == Long.class) {
			return value.longValue();
		}
		return value;
	}

	private static Object castShortValue(Class<?> parameterClass, Short value) {
		if (parameterClass == byte.class || parameterClass == Byte.class) {
			return value.byteValue();
		}
		if (parameterClass == short.class || parameterClass == Short.class) {
			return value;
		}
		if (parameterClass == int.class || parameterClass == Integer.class) {
			return value.intValue();
		}
		if (parameterClass == long.class || parameterClass == Long.class) {
			return value.longValue();
		}
		return value;
	}

	private static Object castIntValue(Class<?> parameterClass, Integer value) {
		if (parameterClass == byte.class || parameterClass == Byte.class) {
			return value.byteValue();
		}
		if (parameterClass == short.class || parameterClass == Short.class) {
			return value.shortValue();
		}
		if (parameterClass == int.class || parameterClass == Integer.class) {
			return value;
		}
		if (parameterClass == long.class || parameterClass == Long.class) {
			return value.longValue();
		}
		return value;
	}

	private static Object castLongValue(Class<?> parameterClass, Long value) {
		if (parameterClass == byte.class || parameterClass == Byte.class) {
			return value.byteValue();
		}
		if (parameterClass == short.class || parameterClass == Short.class) {
			return value.shortValue();
		}
		if (parameterClass == int.class || parameterClass == Integer.class) {
			return value.intValue();
		}
		if (parameterClass == long.class || parameterClass == Long.class) {
			return value;
		}
		return value;
	}

}
