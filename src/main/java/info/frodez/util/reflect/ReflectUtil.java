package info.frodez.util.reflect;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类
 * @author Frodez
 * @date 2019-01-13
 */
public class ReflectUtil {

	@SuppressWarnings("rawtypes")
	private static final Map<String, ConstructorAccess> constructorCache = new ConcurrentHashMap<>();

	/**
	 * 高效获取某类实例
	 * @author Frodez
	 * @date 2019-01-16
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> klass) {
		if(constructorCache.containsKey(klass.getName())) {
			ConstructorAccess<T> constructor = constructorCache.get(klass.getName());
			return constructor.newInstance();
		} else {
			constructorCache.put(klass.getName(), ConstructorAccess.get(klass))
		}
	}

	/**
	 * 获取方法全限定名
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getFullName(Method method) {
		return method.getDeclaringClass().getName() + "." + method.getName();
	}

	/**
	 * 根据方法名获取方法,不支持重载方法<br>
	 * 重载方法会返回方法列表中的第一个<br>
	 * @author Frodez
	 * @date 2018-12-17
	 */
	public static Method getMethod(Class<?> klass, String methodName) throws RuntimeException {
		Method[] methods = klass.getMethods();
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		throw new RuntimeException("方法不存在!");
	}

	/**
	 * 基本数据类型转换<br>
	 * 涉及类型:byte, short, int, long以及对应装箱类
	 * @author Frodez
	 * @date 2018-12-17
	 */
	public static Object castValue(Object value, Class<?> parameterClass) {
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
