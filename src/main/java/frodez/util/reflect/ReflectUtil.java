package frodez.util.reflect;

import frodez.util.beans.pair.Pair;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

/**
 * 反射工具类
 * @author Frodez
 * @date 2019-01-13
 */
@UtilityClass
public class ReflectUtil {

	private static final Map<String, Pair<FastClass, List<FastMethod>>> CGLIB_CACHE = new ConcurrentHashMap<>();

	public static FastMethod getFastMethod(Class<?> klass, String method, Class<?>... params) {
		String className = klass.getName();
		Pair<FastClass, List<FastMethod>> pair = CGLIB_CACHE.get(className);
		if (pair == null) {
			FastClass fastClass = FastClass.create(klass);
			int index = fastClass.getIndex(method, params);
			if (index < 0) {
				throw new NoSuchElementException();
			}
			List<FastMethod> methods = new ArrayList<>(fastClass.getMaxIndex());
			FastMethod fastMethod = fastClass.getMethod(method, params);
			methods.set(index, fastMethod);
			pair = new Pair<>();
			pair.setKey(fastClass);
			pair.setValue(methods);
			CGLIB_CACHE.put(className, pair);
			return fastMethod;
		}
		FastClass fastClass = pair.getKey();
		int index = fastClass.getIndex(method, params);
		if (index < 0) {
			throw new NoSuchElementException();
		}
		List<FastMethod> methods = pair.getValue();
		FastMethod fastMethod = methods.get(index);
		if (fastMethod == null) {
			fastMethod = fastClass.getMethod(method, params);
			methods.set(index, fastMethod);
		}
		return fastMethod;
	}

	/**
	 * 获取方法全限定名
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getFullMethodName(Method method) {
		return method.getDeclaringClass().getName() + "." + method.getName();
	}

	/**
	 * 获取方法全限定名
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getShortMethodName(Method method) {
		return method.getDeclaringClass().getSimpleName() + "." + method.getName();
	}

	/**
	 * 基本数据类型转换<br>
	 * 涉及类型:byte, short, int, long以及对应装箱类
	 * @author Frodez
	 * @date 2018-12-17
	 */
	public static Object baseRevert(Object value, Class<?> parameterClass) {
		Objects.requireNonNull(parameterClass);
		if (value == null) {
			return null;
		}
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
