package info.frodez.util.reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.cglib.beans.BeanCopier;

/**
 * Java Bean工具类
 * @author Frodez
 * @date 2019-01-15
 */
public class BeanUtil {

	private static final Map<String, BeanCopier> copierCache = new ConcurrentHashMap<>();

	/**
	 * copy对象属性
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static void copy(Object source, Object target) {
		String key = source.getClass().getName() + target.getClass().getName();
		BeanCopier copier = null;
		if (!copierCache.containsKey(key)) {
			copier = BeanCopier.create(source.getClass(), target.getClass(), false);
			copierCache.put(key, copier);
		} else {
			copier = copierCache.get(key);
		}
		copier.copy(source, target, null);
	}

}
