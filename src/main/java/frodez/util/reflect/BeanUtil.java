package frodez.util.reflect;

import frodez.util.common.StrUtil;
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

	private static final Map<Class<?>, List<FastMethod>> setterCache = new ConcurrentHashMap<>();

	private static final Map<Class<?>, List<FastMethod>> notNullFieldSetterCache = new ConcurrentHashMap<>();

	private static final Object[] NULL_PARAM = new Object[] { null };

	private static BeanCopier getCopier(Object source, Object target) {
		return COPIER_CACHE.computeIfAbsent(StrUtil.concat(source.getClass().getName(), target.getClass().getName()),
			i -> BeanCopier.create(source.getClass(), target.getClass(), false));
	}

	/**
	 * copy对象属性<br>
	 * 建议对数据库insert时使用本方法，update时使用cover方法。<br>
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static void copy(Object source, Object target) {
		getCopier(source, target).copy(source, target, null);
	}

	/**
	 * 创造一个不具有初始值的,copy自原对象属性的bean<br>
	 * 建议对数据库update时使用本方法，insert时使用copy方法。<br>
	 * 在使用本方法和使用BeanUtil.cover(Object, Object)方法的意义相同时,建议使用本方法,速度更快。<br>
	 * @see frodez.util.reflect.BeanUtil#cover(Object, Object)
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public static <T> T initialize(Object source, Class<T> target) {
		T bean = clearInstance(target);
		getCopier(source, bean).copy(source, bean, null);
		return bean;
	}

	/**
	 * 清空目标bean的所有属性,然后copy原对象属性.<br>
	 * 建议对数据库update时使用本方法，insert时使用copy方法。<br>
	 * 在使用本方法和使用BeanUtil.initialize(Object, Class)方法的意义相同时,建议使用initialize方法,速度更快。<br>
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
	 * @see frodez.util.reflect.BeanUtil#initialize(Object, Class)
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public static void cover(Object source, Object target) {
		clear(target);
		getCopier(source, target).copy(source, target, null);
	}

	/**
	 * bean转map
	 * @author Frodez
	 * @date 2019-02-08
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> map(Object bean) {
		Assert.notNull(bean, "bean must not be null");
		Map<String, Object> map = new HashMap<>(BeanMap.create(bean));
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

	private static List<FastMethod> getSetters(Class<?> klass) {
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

	private static List<FastMethod> getDefaultNotNullSetters(Object bean) {
		Class<?> klass = bean.getClass();
		List<FastMethod> methods = notNullFieldSetterCache.get(klass);
		if (methods != null) {
			return methods;
		}
		methods = new ArrayList<>();
		FastClass fastClass = FastClass.create(klass);
		for (Field field : klass.getDeclaredFields()) {
			try {
				if (Modifier.PRIVATE == field.getModifiers() && field.trySetAccessible() && field.get(bean) != null) {
					String propertyName = StrUtil.concat("set", StrUtil.upperFirst(field.getName()));
					for (Method method : klass.getMethods()) {
						if (method.getName().equals(propertyName) && method.getReturnType() == void.class && method
							.getParameterCount() == 1 && Modifier.PUBLIC == method.getModifiers()) {
							methods.add(fastClass.getMethod(method));
						}
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
				throw new RuntimeException(e);
			}
		}
		notNullFieldSetterCache.put(klass, methods);
		return methods;
	}

	/**
	 * 获取无默认值的bean,推荐使用本方法,比新建一个对象然后使用BeanUtil.clear更快。
	 * @author Frodez
	 * @param <T>
	 * @date 2019-02-08
	 */
	public static <T> T clearInstance(Class<T> klass) {
		T bean = ReflectUtil.newInstance(klass);
		try {
			for (FastMethod method : getDefaultNotNullSetters(bean)) {
				method.invoke(bean, NULL_PARAM);
			}
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
