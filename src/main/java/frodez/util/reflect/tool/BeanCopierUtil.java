package frodez.util.reflect.tool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.cglib.beans.BeanCopier;

public class BeanCopierUtil {

	private static final Map<String, BeanCopier> copierCache = new ConcurrentHashMap<>();

	public static BeanCopier getCopier(Object source, Object target) {
		String key = source.getClass().getName() + target.getClass().getName();
		if (!copierCache.containsKey(key)) {
			BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
			copierCache.put(key, copier);
			return copier;
		} else {
			return copierCache.get(key);
		}
	}

}
