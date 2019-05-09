package frodez.util.reflect;

import frodez.util.common.StrUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

	private static final Map<Class<?>, FastMethod[]> setterCache = new ConcurrentHashMap<>();

	private static final Map<Class<?>, FastMethod[]> notNullFieldSetterCache = new ConcurrentHashMap<>();

	private static final Object[] NULL_PARAM = new Object[] { null };

	private static BeanCopier getCopier(Object source, Object target) {
		return COPIER_CACHE.computeIfAbsent(StrUtil.concat(source.getClass().getName(), target.getClass().getName()),
			i -> BeanCopier.create(source.getClass(), target.getClass(), false));
	}

	/**
	 * copy对象属性<br>
	 * 建议对数据库insert时使用本方法，update时使用cover方法。<br>
	 * @see frodez.util.reflect.BeanUtil#cover(Object, Object)
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
	 * @see frodez.util.reflect.BeanUtil#copy(Object, Object)
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
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 清空bean的默认值<br>
	 * 如果只需要一个全新的无默认值的对象,建议使用BeanUtil.clearInstance方法,<br>
	 * 原因是本方法会对所有字段执行其setter方法(无法确保只有存在默认值的字段拥有值),开销更大。<br>
	 * @see BeanUtil#clearInstance(Class)
	 * @author Frodez
	 * @param <T>
	 * @date 2019-02-08
	 */
	public static void clear(Object bean) {
		Assert.notNull(bean, "bean must not be null");
		try {
			FastMethod[] methods = getSetters(bean.getClass());
			int length = methods.length;
			for (int i = 0; i < length; i++) {
				methods[i].invoke(bean, NULL_PARAM);
			}
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean isSetter(Method method) {
		return method.getName().startsWith("set") && method.getReturnType() == void.class && method
			.getParameterCount() == 1 && Modifier.PUBLIC == method.getModifiers();
	}

	private static boolean isPrivateAndNotNullField(Field field, Object bean) throws IllegalArgumentException,
		IllegalAccessException {
		return Modifier.PRIVATE == field.getModifiers() && field.trySetAccessible() && field.get(bean) != null;
	}

	private static FastMethod[] getSetters(Class<?> klass) {
		FastMethod[] methods = setterCache.get(klass);
		if (methods == null) {
			List<FastMethod> list = new ArrayList<>();
			FastClass fastClass = FastClass.create(klass);
			for (Method method : klass.getMethods()) {
				if (isSetter(method)) {
					list.add(fastClass.getMethod(method));
				}
			}
			methods = new FastMethod[list.size()];
			methods = list.toArray(methods);
			setterCache.put(klass, methods);
		}
		return methods;
	}

	private static FastMethod[] getDefaultNotNullSetters(Object bean) throws IllegalArgumentException,
		IllegalAccessException {
		Class<?> klass = bean.getClass();
		FastMethod[] methods = notNullFieldSetterCache.get(klass);
		if (methods == null) {
			List<FastMethod> list = new ArrayList<>();
			FastClass fastClass = FastClass.create(klass);
			List<Method> setters = new ArrayList<>();
			for (Method method : klass.getMethods()) {
				if (isSetter(method)) {
					setters.add(method);
				}
			}
			for (Field field : klass.getDeclaredFields()) {
				if (isPrivateAndNotNullField(field, bean)) {
					for (Method method : setters) {
						if (method.getName().endsWith(StrUtil.upperFirst(field.getName()))) {
							list.add(fastClass.getMethod(method));
							break;
						}
					}
				}
			}
			methods = new FastMethod[list.size()];
			methods = list.toArray(methods);
			notNullFieldSetterCache.put(klass, methods);
		}
		return methods;
	}

	/**
	 * 获取无默认值的bean。<br>
	 * 推荐使用本方法,比新建一个对象然后使用BeanUtil.clear更快,<br>
	 * 原因是本方法只会执行存在默认值的字段的setter方法,方法执行的开销减小。<br>
	 * @see BeanUtil#clear(Object)
	 * @author Frodez
	 * @param <T>
	 * @date 2019-02-08
	 */
	public static <T> T clearInstance(Class<T> klass) {
		try {
			T bean = ReflectUtil.newInstance(klass);
			FastMethod[] methods = getDefaultNotNullSetters(bean);
			int length = methods.length;
			for (int i = 0; i < length; i++) {
				methods[i].invoke(bean, NULL_PARAM);
			}
			return bean;
		} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
