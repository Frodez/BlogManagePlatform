package frodez.util.reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

/**
 * Java Bean工具类
 * @author Frodez
 * @date 2019-01-15
 */
public class BeanUtil {

	private static final Map<String, BeanCopier> copierCache = new ConcurrentHashMap<>();

	private static BeanCopier getCopier(Object source, Object target) {
		String key = source.getClass().getName() + target.getClass().getName();
		if (!copierCache.containsKey(key)) {
			BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
			copierCache.put(key, copier);
			return copier;
		} else {
			return copierCache.get(key);
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

	@SuppressWarnings("unchecked")
	public static Map<String, Object> asMap(Object bean) {
		return BeanMap.create(bean);
	}

	public static <T> T asBean(Map<String, Object> map, T bean) {
		BeanMap.create(bean).putAll(map);
		return bean;
	}

}
