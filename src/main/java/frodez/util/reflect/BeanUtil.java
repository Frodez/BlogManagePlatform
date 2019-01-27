package frodez.util.reflect;

import frodez.util.reflect.tool.BeanCopierUtil;

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

}
