package info.frodez.util.reflect;

import org.springframework.cglib.beans.BeanCopier;

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
		BeanCopier.create(source.getClass(), target.getClass(), false).copy(source, target, null);
	}

}
