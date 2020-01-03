package frodez.util.reflect;

import frodez.util.common.PrimitiveUtil;
import frodez.util.common.StrUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.util.Assert;

/**
 * Java Bean工具类<br>
 * 建议不要在项目初始化阶段使用,而用于日常业务或者已经初始化完毕后。<br>
 * @author Frodez
 * @date 2019-01-15
 */
@Slf4j
@UtilityClass
public class BeanUtil {

	private static final Map<String, BeanCopier> COPIER_CACHE = new ConcurrentHashMap<>();

	private static final Map<Class<?>, List<FastMethod>> SETTER_CACHE = new ConcurrentHashMap<>();

	private static final Map<Class<?>, List<FastMethod>> NOT_NULL_FIELD_SETTER_CACHE = new ConcurrentHashMap<>();

	private static BeanCopier getCopier(Object source, Object target) {
		return COPIER_CACHE.computeIfAbsent(StrUtil.concat(source.getClass().getName(), target.getClass().getName()), i -> BeanCopier.create(source
			.getClass(), target.getClass(), false));
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
	 * copy对象属性<br>
	 * 建议对数据库insert时使用本方法，update时使用cover方法。<br>
	 * @see frodez.util.reflect.BeanUtil#cover(Object, Object)
	 * @author Frodez
	 * @param supplier 对象提供者
	 * @date 2019-01-15
	 */
	public static <T> T copy(Object source, Supplier<T> supplier) {
		T target = supplier.get();
		getCopier(source, target).copy(source, target, null);
		return target;
	}

	/**
	 * 批量copy对象属性<br>
	 * @author Frodez
	 * @date 2019-06-19
	 */
	public static <E, T> List<T> copies(List<E> sources, Supplier<T> supplier) {
		Assert.notNull(sources, "sources must not be null");
		return sources.stream().map((iter) -> copy(iter, supplier)).collect(Collectors.toList());
	}

	/**
	 * 创造一个不具有初始值的,copy自原对象属性的bean<br>
	 * 建议对数据库update时使用本方法，insert时使用copy方法。<br>
	 * 在使用本方法和使用BeanUtil.cover(Object, Object)方法的意义相同时,建议使用本方法,速度更快。<br>
	 * @see frodez.util.reflect.BeanUtil#cover(Object, Object)
	 * @author Frodez
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @date 2019-03-10
	 */
	@SneakyThrows
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
	 * @throws InvocationTargetException
	 * @date 2019-03-10
	 */
	@SneakyThrows
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
		return new HashMap<>(BeanMap.create(bean));
	}

	/**
	 * map转bean
	 * @author Frodez
	 * @throws InvocationTargetException
	 * @date 2019-02-08
	 */
	@SneakyThrows
	public static <T> T as(Map<String, Object> map, Supplier<T> supplier) {
		Assert.notNull(map, "map must not be null");
		T bean = supplier.get();
		try {
			BeanMap.create(bean).putAll(map);
		} catch (Exception e) {
			log.error("map转bean出现异常,采用安全方式继续...异常消息:{}", e.getMessage());
			//如果出错则采用安全但较慢的方式
			safeAs(map, bean);
		}
		return bean;
	}

	/**
	 * map转bean
	 * @author Frodez
	 * @throws InvocationTargetException
	 * @date 2019-02-08
	 */
	@SneakyThrows
	public static <T> T as(Map<String, Object> map, Class<T> klass) {
		Assert.notNull(map, "map must not be null");
		T bean = ReflectUtil.instance(klass);
		try {
			BeanMap.create(bean).putAll(map);
		} catch (Exception e) {
			log.error("map转bean出现异常,采用安全方式继续...异常消息:{}", e.getMessage());
			//如果出错则采用安全但较慢的方式
			safeAs(map, bean);
		}
		return bean;
	}

	@SuppressWarnings("unchecked")
	@SneakyThrows
	private static <T> T safeAs(Map<String, Object> map, T bean) {
		List<FastMethod> methods = setters(bean.getClass());
		for (FastMethod fastMethod : methods) {
			String field = StrUtil.lowerFirst(fastMethod.getName().substring(3));
			Object value = map.get(field);
			if (value == null) {
				continue;
			}
			if (PrimitiveUtil.isBaseType(value.getClass())) {
				value = PrimitiveUtil.cast(value, fastMethod.getParameterTypes()[0]);
				fastMethod.invoke(bean, new Object[] { value });
			} else {
				fastMethod.invoke(bean, new Object[] { value });
			}
		}
		return bean;
	}

	/**
	 * 清空bean的默认值<br>
	 * 如果只需要一个全新的无默认值的对象,建议使用BeanUtil.clearInstance方法,<br>
	 * 原因是本方法会对所有字段执行其setter方法(无法确保只有存在默认值的字段拥有值),开销更大。<br>
	 * @see BeanUtil#clearInstance(Class)
	 * @author Frodez
	 * @param <T>
	 * @throws InvocationTargetException
	 * @date 2019-02-08
	 */
	@SneakyThrows
	public static void clear(Object bean) {
		Assert.notNull(bean, "bean must not be null");
		List<FastMethod> methods = setters(bean.getClass());
		int length = methods.size();
		for (int i = 0; i < length; i++) {
			methods.get(i).invoke(bean, ReflectUtil.EMPTY_ARRAY);
		}
	}

	private static boolean isSetter(Method method) {
		return method.getName().startsWith("set") && method.getReturnType() == void.class && method.getParameterCount() == 1
			&& Modifier.PUBLIC == method.getModifiers();
	}

	private static boolean isPrivateAndNotNullField(Field field, Object bean) throws IllegalArgumentException, IllegalAccessException {
		return Modifier.PRIVATE == field.getModifiers() && field.trySetAccessible() && field.get(bean) != null;
	}

	/**
	 * 获取setter对应的field
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static List<Field> getSetterFields(Class<?> klass) {
		Assert.notNull(klass, "klass must not be null");
		List<Field> fields = new ArrayList<>();
		List<Method> setters = new ArrayList<>();
		for (Method method : klass.getMethods()) {
			if (isSetter(method)) {
				setters.add(method);
			}
		}
		for (Field field : klass.getDeclaredFields()) {
			if (Modifier.PRIVATE == field.getModifiers()) {
				for (Method method : setters) {
					if (method.getName().endsWith(StrUtil.upperFirst(field.getName()))) {
						fields.add(field);
						break;
					}
				}
			}
		}
		return Collections.unmodifiableList(fields);
	}

	/**
	 * 获取所有setters
	 * @author Frodez
	 * @date 2019-05-22
	 */
	public static List<FastMethod> getSetters(Class<?> klass) {
		Assert.notNull(klass, "klass must not be null");
		return Collections.unmodifiableList(setters(klass));
	}

	private static List<FastMethod> setters(Class<?> klass) {
		List<FastMethod> methods = SETTER_CACHE.get(klass);
		if (methods == null) {
			methods = new ArrayList<>();
			FastClass fastClass = FastClass.create(klass);
			for (Method method : klass.getMethods()) {
				if (isSetter(method)) {
					methods.add(fastClass.getMethod(method));
				}
			}
			SETTER_CACHE.put(klass, methods);
		}
		return methods;
	}

	/**
	 * 获取所有非空的setters
	 * @author Frodez
	 * @date 2019-05-22
	 */
	@SneakyThrows
	public static List<FastMethod> getDefaultNotNullSetters(Class<?> klass) {
		Assert.notNull(klass, "klass must not be null");
		return Collections.unmodifiableList(defaultNotNullSetters(ReflectUtil.instance(klass)));
	}

	private static List<FastMethod> defaultNotNullSetters(Object bean) throws IllegalArgumentException, IllegalAccessException {
		Class<?> klass = bean.getClass();
		List<FastMethod> methods = NOT_NULL_FIELD_SETTER_CACHE.get(klass);
		if (methods == null) {
			methods = new ArrayList<>();
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
							methods.add(fastClass.getMethod(method));
							break;
						}
					}
				}
			}
			NOT_NULL_FIELD_SETTER_CACHE.put(klass, methods);
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
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2019-02-08
	 */
	@SneakyThrows
	public static <T> T clearInstance(Class<T> klass) {
		T bean = ReflectUtil.instance(klass);
		List<FastMethod> methods = defaultNotNullSetters(bean);
		int length = methods.size();
		for (int i = 0; i < length; i++) {
			methods.get(i).invoke(bean, ReflectUtil.EMPTY_ARRAY);
		}
		return bean;
	}

}
