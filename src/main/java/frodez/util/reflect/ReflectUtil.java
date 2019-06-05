package frodez.util.reflect;

import frodez.util.beans.pair.Pair;
import frodez.util.common.StrUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 反射工具类<br>
 * 建议不要在项目初始化阶段使用,而用于日常业务或者已经初始化完毕后。<br>
 * @author Frodez
 * @date 2019-01-13
 */
@UtilityClass
public class ReflectUtil {

	public static final Object[] EMPTY_ARRAY_OBJECTS = new Object[] { null };

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
	 * @throws NoSuchMethodException
	 * @date 2019-04-12
	 */
	public static FastMethod getFastMethod(Class<?> klass, String method, Class<?>... params)
		throws NoSuchMethodException {
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
	 * 获取字段全限定名
	 * @author Frodez
	 * @date 2019-06-05
	 */
	public static String getFullFieldName(Field field) {
		return StrUtil.concat(field.getDeclaringClass().getName(), ".", field.getName());
	}

	/**
	 * 获取字段全限定名
	 * @author Frodez
	 * @date 2019-06-05
	 */
	public static String getShortFieldName(Field field) {
		return StrUtil.concat(field.getDeclaringClass().getSimpleName(), ".", field.getName());
	}

	/**
	 * 将两个数转换为指定的类型然后进行比较<br>
	 * 涉及类型:byte, short, int, long以及对应装箱类
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static int compareTo(Object first, Object second, Class<?> baseClass) {
		Assert.notNull(first, "first must not be null");
		Assert.notNull(second, "second must not be null");
		Assert.notNull(baseClass, "baseClass must not be null");
		if (baseClass == Byte.class || baseClass == byte.class) {
			return Byte.compare(primitiveAdapt(first, Byte.class), primitiveAdapt(second, Byte.class));
		} else if (baseClass == Short.class || baseClass == short.class) {
			return Short.compare(primitiveAdapt(first, Short.class), primitiveAdapt(second, Short.class));
		} else if (baseClass == Integer.class || baseClass == int.class) {
			return Integer.compare(primitiveAdapt(first, Integer.class), primitiveAdapt(second, Integer.class));
		} else if (baseClass == Long.class || baseClass == Long.class) {
			return Long.compare(primitiveAdapt(first, long.class), primitiveAdapt(second, Long.class));
		} else {
			throw new UnsupportedOperationException("只能用于byte, short, int, long以及对应装箱类!");
		}
	}

	/**
	 * 基本数据类型适配<br>
	 * value可为空<br>
	 * 涉及类型:byte, short, int, long以及对应装箱类,还有void
	 * @author Frodez
	 * @param <T>
	 * @date 2018-12-17
	 */
	public static <T> T primitiveAdapt(@Nullable Object value, Class<T> parameterClass) {
		Assert.notNull(parameterClass, "parameterClass must not be null");
		if (value == null) {
			return null;
		}
		Class<?> valueClass = value.getClass();
		if (valueClass == Byte.class || valueClass == byte.class) {
			return castByteValue(parameterClass, (Byte) value);
		} else if (valueClass == Short.class || valueClass == short.class) {
			return castShortValue(parameterClass, (Short) value);
		} else if (valueClass == Integer.class || valueClass == int.class) {
			return castIntValue(parameterClass, (Integer) value);
		} else if (valueClass == Long.class || valueClass == Long.class) {
			return castLongValue(parameterClass, (Long) value);
		}
		throw new UnsupportedOperationException("只能用于byte, short, int, long以及对应装箱类,以及void类型!");
	}

	@SuppressWarnings("unchecked")
	private static <T> T castByteValue(Class<T> parameterClass, Byte value) {
		if (parameterClass == Byte.class || parameterClass == byte.class) {
			return (T) value;
		}
		if (parameterClass == Short.class || parameterClass == short.class) {
			return (T) Short.valueOf(value.shortValue());
		}
		if (parameterClass == Integer.class || parameterClass == int.class) {
			return (T) Integer.valueOf(value.intValue());
		}
		if (parameterClass == Long.class || parameterClass == long.class) {
			return (T) Long.valueOf(value.longValue());
		}
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	private static <T> T castShortValue(Class<T> parameterClass, Short value) {
		if (parameterClass == Byte.class || parameterClass == byte.class) {
			return (T) Byte.valueOf(value.byteValue());
		}
		if (parameterClass == Short.class || parameterClass == short.class) {
			return (T) value;
		}
		if (parameterClass == Integer.class || parameterClass == int.class) {
			return (T) Integer.valueOf(value.intValue());
		}
		if (parameterClass == Long.class || parameterClass == long.class) {
			return (T) Long.valueOf(value.longValue());
		}
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	private static <T> T castIntValue(Class<T> parameterClass, Integer value) {
		if (parameterClass == Byte.class || parameterClass == byte.class) {
			return (T) Byte.valueOf(value.byteValue());
		}
		if (parameterClass == Short.class || parameterClass == short.class) {
			return (T) Short.valueOf(value.shortValue());
		}
		if (parameterClass == Integer.class || parameterClass == int.class) {
			return (T) value;
		}
		if (parameterClass == Long.class || parameterClass == long.class) {
			return (T) Long.valueOf(value.longValue());
		}
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	private static <T> T castLongValue(Class<T> parameterClass, Long value) {
		if (parameterClass == Byte.class || parameterClass == byte.class) {
			return (T) Byte.valueOf(value.byteValue());
		}
		if (parameterClass == Short.class || parameterClass == short.class) {
			return (T) Short.valueOf(value.shortValue());
		}
		if (parameterClass == Integer.class || parameterClass == int.class) {
			return (T) Integer.valueOf(value.intValue());
		}
		if (parameterClass == Long.class || parameterClass == long.class) {
			return (T) value;
		}
		return (T) value;
	}

}
