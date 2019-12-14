package frodez.util.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

/**
 * 判空工具类<br>
 * yes方法用于判断对象是否为空,no方法用于判断对象是否不为空
 * @author Frodez
 * @date 2019-02-17
 */
@UtilityClass
public class EmptyUtil {

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(boolean[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(boolean[] array) {
		return array != null && array.length != 0;
	}

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(byte[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(byte[] array) {
		return array != null && array.length != 0;
	}

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(char[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(char[] array) {
		return array != null && array.length != 0;
	}

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(short[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(short[] array) {
		return array != null && array.length != 0;
	}

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(int[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(int[] array) {
		return array != null && array.length != 0;
	}

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(long[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(long[] array) {
		return array != null && array.length != 0;
	}

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(float[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(float[] array) {
		return array != null && array.length != 0;
	}

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(double[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(double[] array) {
		return array != null && array.length != 0;
	}

	/**
	 * 判断对象是否为空,默认关闭严格模式(即所有成员均不为空)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(Object[] array) {
		return yes(false, array);
	}

	/**
	 * 判断对象是否为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(boolean strictMode, Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		if (!strictMode) {
			return false;
		}
		for (Object object : array) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断对象是否不为空,默认关闭严格模式(即所有成员均不为空)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(Object[] array) {
		return no(false, array);
	}

	/**
	 * 判断对象是否不为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(boolean strictMode, Object[] array) {
		if (array == null || array.length == 0) {
			return false;
		}
		if (!strictMode) {
			return true;
		}
		for (Object object : array) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断对象是否为空,默认关闭严格模式(即所有成员均不为空)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(Collection<?> collection) {
		return yes(false, collection);
	}

	/**
	 * 判断对象是否为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(boolean strictMode, Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		if (!strictMode) {
			return false;
		}
		for (Object object : collection) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断对象是否不为空,默认关闭严格模式(即所有成员均不为空)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(Collection<?> collection) {
		return no(false, collection);
	}

	/**
	 * 判断对象是否不为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(boolean strictMode, Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return false;
		}
		if (!strictMode) {
			return true;
		}
		for (Object object : collection) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断对象是否为空,默认关闭严格模式(即所有成员均不为空)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(Map<?, ?> map) {
		return yes(false, map);
	}

	/**
	 * 判断对象是否为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(boolean strictMode, Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return true;
		}
		if (!strictMode) {
			return false;
		}
		for (Object object : map.values()) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断对象是否不为空,默认关闭严格模式(即所有成员均不为空)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(Map<?, ?> map) {
		return no(false, map);
	}

	/**
	 * 判断对象是否不为空<br>
	 * strictMode为true时开启严格模式,为false时关闭严格模式。
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(boolean strictMode, Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return false;
		}
		if (!strictMode) {
			return true;
		}
		for (Object object : map.values()) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否为空<br>
	 * <strong>警告:与其他方法不同的是,长度为0的空字符串也会被判定为空。这一行为与对数组的判断类似,但并不完全相同。</strong>
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(CharSequence charSequence) {
		return charSequence == null || charSequence.length() == 0;
	}

	/**
	 * 判断字符串是否不为空<br>
	 * <strong>警告:与其他方法不同的是,长度为0的空字符串也会被判定为空。这一行为与对数组的判断类似,但并不完全相同。</strong>
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(CharSequence charSequence) {
		return charSequence != null && charSequence.length() != 0;
	}

	/**
	 * 将数组中的null元素剔除
	 * @author Frodez
	 * @date 2019-12-14
	 */
	public static Object[] trim(Object[] objects) {
		Assert.notNull(objects, "objects must not be null");
		int size = 0;
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				size++;
			}
		}
		if (size == 0) {
			return new Object[0];
		}
		Object[] result = new Object[size];
		for (int i = 0, j = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				result[j] = objects[i];
				j++;
			}
		}
		return result;
	}

	/**
	 * 将数组中的null元素剔除
	 * @author Frodez
	 * @date 2019-12-14
	 */
	public static CharSequence[] trim(CharSequence[] objects) {
		Assert.notNull(objects, "objects must not be null");
		int size = 0;
		for (int i = 0; i < objects.length; i++) {
			if (EmptyUtil.no(objects[i])) {
				size++;
			}
		}
		if (size == 0) {
			return new CharSequence[0];
		}
		CharSequence[] result = new CharSequence[size];
		for (int i = 0, j = 0; i < objects.length; i++) {
			if (EmptyUtil.no(objects[i])) {
				result[j] = objects[i];
				j++;
			}
		}
		return result;
	}

	/**
	 * 将List中的null元素剔除
	 * @author Frodez
	 * @date 2019-12-14
	 */
	public static Object[] trim(List<Object> objects) {
		Assert.notNull(objects, "objects must not be null");
		int size = 0;
		for (Object object : objects) {
			if (object != null) {
				size++;
			}
		}
		if (size == 0) {
			return new Object[0];
		}
		Object[] result = new Object[size];
		int j = 0;
		for (Object object : objects) {
			if (object != null) {
				result[j] = object;
				j++;
			}
		}
		return result;
	}

}
