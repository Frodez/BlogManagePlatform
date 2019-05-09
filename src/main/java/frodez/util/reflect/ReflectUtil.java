package frodez.util.reflect;

import frodez.util.beans.pair.Pair;
import frodez.util.common.StrUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 反射工具类
 * @author Frodez
 * @date 2019-01-13
 */
@UtilityClass
public class ReflectUtil {

	private static final Map<Class<?>, Pair<FastClass, FastMethod[]>> CGLIB_CACHE = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> klass) throws InvocationTargetException {
		return (T) getFastClass(klass).newInstance();
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
	 * @date 2019-04-12
	 */
	public static FastMethod getFastMethod(Class<?> klass, String method, Class<?>... params) {
		Assert.notNull(klass, "klass must not be null");
		Assert.notNull(method, "method must not be null");
		Pair<FastClass, FastMethod[]> pair = CGLIB_CACHE.get(klass);
		if (pair == null) {
			FastClass fastClass = FastClass.create(klass);
			FastMethod[] methods = new FastMethod[fastClass.getMaxIndex() + 1];
			int index = fastClass.getIndex(method, params);
			if (index < 0) {
				throw new NoSuchElementException();
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
			throw new NoSuchElementException();
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
		return StrUtil.concat(method.getDeclaringClass().getName(), ".", method.getName());
	}

	/**
	 * 获取方法全限定名
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getShortMethodName(Method method) {
		return StrUtil.concat(method.getDeclaringClass().getSimpleName(), ".", method.getName());
	}

	/**
	 * 基本数据类型适配<br>
	 * value可为空<br>
	 * 涉及类型:byte, short, int, long以及对应装箱类,还有void
	 * @author Frodez
	 * @date 2018-12-17
	 */
	public static Object primitiveAdapt(@Nullable Object value, Class<?> parameterClass) {
		Assert.notNull(parameterClass, "parameterClass must not be null");
		if (value == null) {
			return null;
		}
		Class<?> valueClass = value.getClass();
		if (valueClass == byte.class || valueClass == Byte.class) {
			return castByteValue(parameterClass, (Byte) value);
		} else if (valueClass == short.class || valueClass == Short.class) {
			return castShortValue(parameterClass, (Short) value);
		} else if (valueClass == int.class || valueClass == Integer.class) {
			return castIntValue(parameterClass, (Integer) value);
		} else if (valueClass == long.class || valueClass == Long.class) {
			return castLongValue(parameterClass, (Long) value);
		}
		throw new IllegalArgumentException("只能用于byte, short, int, long以及对应装箱类,以及void类型!");
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
