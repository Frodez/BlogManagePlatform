package frodez.util.reflect;

import frodez.util.beans.pair.Pair;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.util.Assert;

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

	private static final int PROPERTY_MODIFIER = Modifier.PRIVATE;

	private static final int GETTER_SETTER_MODIFIER = Modifier.PUBLIC;

	private static BeanCopier getCopier(Object source, Object target) {
		return COPIER_CACHE.computeIfAbsent(source.getClass().getName().concat(target.getClass().getName()).toString(),
			i -> BeanCopier.create(source.getClass(), target.getClass(), false));
	}

	/**
	 * copy对象属性<br>
	 * 建议insert时使用本方法，update时使用cover方法。<br>
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static void copy(Object source, Object target) {
		getCopier(source, target).copy(source, target, null);
	}

	/**
	 * 清空目标bean的所有属性,然后copy原对象属性.<br>
	 * 建议update时使用本方法，insert时使用copy方法。<br>
	 * 注意:请不要这样使用本方法:<br>
	 *
	 * <pre>
	 * Bean one = new Bean();
	 * Bean two = new Bean();
	 * BeanUtil.cover(one, two);
	 * </pre>
	 *
	 * 因为本方法先清空属性然后赋值,所以如果这样做,未清空属性的bean会将清空部分覆盖.<br>
	 * 请务必考虑清楚,原对象不为null的属性有哪些.<br>
	 * 否则这样使用等于无用功.<br>
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public static void cover(Object source, Object target) {
		clear(target);
		getCopier(source, target).copy(source, target, null);
	}

	/**
	 * 创造一个不具有初始值的,copy自原对象属性的bean<br>
	 * 建议update时使用本方法，insert时使用copy方法。<br>
	 * 注意:请不要这样使用本方法:<br>
	 *
	 * <pre>
	 * Bean one = new Bean();
	 * Bean two = new Bean();
	 * BeanUtil.cover(one, two);
	 * </pre>
	 *
	 * 因为本方法先清空属性然后赋值,所以如果这样做,未清空属性的bean会将清空部分覆盖.<br>
	 * 请务必考虑清楚,原对象不为null的属性有哪些.<br>
	 * 否则这样使用等于无用功.<br>
	 * @author Frodez
	 * @param <T>
	 * @date 2019-03-10
	 */
	public static <T> T cover(Object source, Class<T> target) {
		T bean = clearInstance(target);
		getCopier(source, bean).copy(source, bean, null);
		return bean;
	}

	/**
	 * bean转map
	 * @author Frodez
	 * @date 2019-02-08
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> map(Object bean) {
		Assert.notNull(bean, "bean must not be null");
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
		Assert.notNull(map, "map must not be null");
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
		Assert.notNull(bean, "bean must not be null");
		try {
			for (Field field : bean.getClass().getDeclaredFields()) {
				if (PROPERTY_MODIFIER == field.getModifiers() && field.trySetAccessible() && field.get(bean) != null) {
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
		Assert.notNull(bean, "bean must not be null");
		try {
			for (FastMethod method : getSetters(bean.getClass())) {
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
				if (GETTER_SETTER_MODIFIER == method.getModifiers()) {
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
					properties.put(String.valueOf(Character.toLowerCase(propertyName.charAt(0))).concat(propertyName
						.substring(1)), pair);
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
