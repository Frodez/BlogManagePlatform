package frodez.util.reflect;

import com.esotericsoftware.reflectasm.FieldAccess;

import frodez.util.reflect.tool.BeanCopierUtil;
import frodez.util.reflect.tool.ReflectASMUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Java Bean工具类
 * @author Frodez
 * @date 2019-01-15
 */
public class BeanUtil {

	/**
	 * copy对象属性
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static void copy(Object source, Object target) {
		BeanCopierUtil.getCopier(source, target).copy(source, target, null);
	}

	/**
	 * 对象转map
	 * @author Frodez
	 * @date 2019-01-22
	 */
	public static Map<String, Object> beanToMap(Object bean) {
		FieldAccess fieldAccess = ReflectASMUtil.getFields(bean.getClass());
		Map<String, Object> result = new HashMap<>();
		try {
			for (Field field : fieldAccess.getFields()) {
				result.put(field.getName(), field.get(bean));
			}
			return result;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return new HashMap<>();
		}
	}

}
