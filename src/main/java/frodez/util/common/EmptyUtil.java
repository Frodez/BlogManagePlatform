package frodez.util.common;

import java.util.Collection;
import java.util.Map;
import lombok.experimental.UtilityClass;

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
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(Object[] array) {
		return array != null && array.length != 0;
	}

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(Map<?, ?> map) {
		return map != null && !map.isEmpty();
	}

	/**
	 * 判断对象是否为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean yes(String string) {
		return string == null || string.length() == 0;
	}

	/**
	 * 判断对象是否不为空
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static boolean no(String string) {
		return string != null && string.length() != 0;
	}

}
