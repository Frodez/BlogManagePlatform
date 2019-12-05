package frodez.util.reflect;

import frodez.constant.settings.DefStr;
import frodez.util.beans.pair.Pair;
import frodez.util.common.StrUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
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

	/**
	 * 对象实例化
	 * @author Frodez
	 * @date 2019-06-19
	 */
	@SneakyThrows
	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> klass) {
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

}
