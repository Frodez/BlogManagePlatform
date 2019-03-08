package frodez.util.reflect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.Assert;

/**
 * Java Bean工具类
 * @author Frodez
 * @date 2019-01-15
 */
public class BeanUtil {

	private static final Map<String, BeanCopier> COPIER_CACHE = new ConcurrentHashMap<>();

	private static BeanCopier getCopier(Object source, Object target) {
		String key = source.getClass().getName() + target.getClass().getName();
		BeanCopier copier = COPIER_CACHE.get(key);
		if (copier == null) {
			copier = BeanCopier.create(source.getClass(), target.getClass(), false);
			COPIER_CACHE.put(key, copier);
			return copier;
		} else {
			return COPIER_CACHE.get(key);
		}
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
	public static Map<String, Object> map(@Nullable Object bean) {
		Assert.notNull(bean, "bean不能为空!");
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
		Assert.notNull(map, "map不能为空!");
		try {
			T bean = klass.getDeclaredConstructor().newInstance();
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
		Assert.notNull(bean, "参数不能为空!");
		try {
			for (Field field : bean.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				if (field.get(bean) != null) {
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
		Assert.notNull(bean, "参数不能为空!");
		try {
			for (Field field : bean.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				field.set(bean, null);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取无默认值的bean
	 * @author Frodez
	 * @param <T>
	 * @date 2019-02-08
	 */
	public static <T> T clearInstance(Class<T> klass) {
		Assert.notNull(klass, "类型不能为空!");
		try {
			T bean = klass.getDeclaredConstructor().newInstance();
			for (Field field : klass.getDeclaredFields()) {
				field.setAccessible(true);
				field.set(bean, null);
			}
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
