package frodez.util.reflect;

import frodez.util.beans.pair.Pair;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

/**
 * Java Bean工具类
 * @author Frodez
 * @date 2019-01-15
 */
@UtilityClass
public class BeanUtil {

	private static final Map<String, BeanCopier> COPIER_CACHE = new ConcurrentHashMap<>();

	private static final Map<Class<?>, Map<String, Pair<FastMethod, FastMethod>>> propertyCache =
		new ConcurrentHashMap<>();

	private static final Map<Class<?>, List<FastMethod>> setterCache = new ConcurrentHashMap<>();

	private static final Object[] NULL_PARAM = new Object[] { null };

	private static BeanCopier getCopier(Object source, Object target) {
		return COPIER_CACHE.computeIfAbsent(new StringBuilder(source.getClass().getName()).append(target.getClass()
			.getName()).toString(), i -> BeanCopier.create(source.getClass(), target.getClass(), false));
	}

	/**
	 * copy对象属性
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static void copy(Object source, Object target) {
		getCopier(source, target).copy(source, target, null);
	}

	/**
	 * bean转map
	 * @author Frodez
	 * @date 2019-02-08
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> map(Object bean) {
		Objects.requireNonNull(bean);
		Map<String, Object> map = new HashMap<>();
		map.putAll(BeanMap.create(bean));
		return map;
	}

	/**
	 * map转bean
	 * @author Frodez
	 * @date 2019-02-08
	 */
	public static <T> T as(Map<String, Object> map, Class<T> klass) {
		Objects.requireNonNull(map);
		try {
			T bean = ReflectUtil.newInstance(klass);
			BeanMap.create(bean).putAll(map);
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 判断bean的默认值是否已全部清空
	 * @author Frodez
	 * @date 2019-02-08
	 */
	public static boolean isClear(Object bean) {
		Objects.requireNonNull(bean);
		try {
			for (Field field : bean.getClass().getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()) && field
					.trySetAccessible() && field.get(bean) != null) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 清空bean的默认值
	 * @author Frodez
	 * @param <T>
	 * @date 2019-02-08
	 */
	public static void clear(Object bean) {
		Objects.requireNonNull(bean);
		try {
			List<FastMethod> methods = getSetters(bean.getClass());
			for (FastMethod method : methods) {
				method.invoke(bean, NULL_PARAM);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<FastMethod> getSetters(Class<?> klass) {
		List<FastMethod> methods = setterCache.get(klass);
		if (methods == null) {
			methods = new ArrayList<>();
			FastClass fastClass = FastClass.create(klass);
			for (Method method : klass.getMethods()) {
				if (method.getName().startsWith("set") && method.getReturnType() == void.class && method
					.getParameterCount() == 1) {
					methods.add(fastClass.getMethod(method));
				}
			}
			setterCache.put(klass, methods);
		}
		return methods;
	}

	public Map<String, Pair<FastMethod, FastMethod>> getProperties(Class<?> klass) {
		Map<String, Pair<FastMethod, FastMethod>> properties = propertyCache.get(klass);
		if (properties == null) {
			properties = new HashMap<>();
			FastClass fastClass = FastClass.create(klass);
			List<FastMethod> methods = new ArrayList<>();
			for (Method method : klass.getMethods()) {
				if (Modifier.isPublic(method.getModifiers()) && !Modifier.isNative(method.getModifiers()) && !Modifier
					.isFinal(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
					methods.add(fastClass.getMethod(method));
				}
			}
			for (FastMethod method : methods) {
				if (method.getName().startsWith("set") && method.getReturnType() == void.class && method
					.getParameterTypes().length == 1) {
					String propertyName = method.getName().substring(3);
					FastMethod getter = null;
					for (FastMethod iter : methods) {
						if (iter.getName().endsWith(propertyName) && (iter.getName().startsWith("get") || iter.getName()
							.startsWith("is"))) {
							getter = iter;
							break;
						}
					}
					Pair<FastMethod, FastMethod> pair = new Pair<>();
					pair.setKey(getter);
					pair.setValue(method);
					properties.put(new StringBuilder().append(Character.toLowerCase(propertyName.charAt(0))).append(
						propertyName.substring(1)).toString(), pair);
				}
			}
		}
		return properties;
	}

	/**
	 * 获取无默认值的bean
	 * @author Frodez
	 * @param <T>
	 * @date 2019-02-08
	 */
	public static <T> T clearInstance(Class<T> klass) {
		T bean = ReflectUtil.newInstance(klass);
		clear(bean);
		return bean;
	}

}
