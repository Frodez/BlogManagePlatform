package frodez.util.common;

import lombok.experimental.UtilityClass;

/**
 * 布尔逻辑工具类
 * @author Frodez
 * @date 2019-12-09
 */
@UtilityClass
public class BoolUtil {

	/**
	 * 与<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 1<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean and(boolean a, boolean b) {
		return a && b;
	}

	/**
	 * 与<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 1<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean and(Boolean a, Boolean b) {
		return a && b;
	}

	/**
	 * 与<br>
	 * 以null为false,exist为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 1<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean and(Object a, Object b) {
		return and(a != null, b != null);
	}

	/**
	 * 与非<br>
	 * 以null为false,exist为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 1<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 0<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean nand(Object a, Object b) {
		return !and(a != null, b != null);
	}

	/**
	 * 或<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 1<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean or(boolean a, boolean b) {
		return a || b;
	}

	/**
	 * 或<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 1<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean or(Boolean a, Boolean b) {
		return a || b;
	}

	/**
	 * 或<br>
	 * 以null为false,exist为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 1<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean or(Object a, Object b) {
		return or(a != null, b != null);
	}

	/**
	 * 或非<br>
	 * 以null为false,exist为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 1<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 0<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean nor(Object a, Object b) {
		return !or(a != null, b != null);
	}

	/**
	 * 异或<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 0<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean xor(boolean a, boolean b) {
		return a ^ b;
	}

	/**
	 * 异或<br>
	 * 以0为false,1为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 0<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean xor(Boolean a, Boolean b) {
		return a ^ b;
	}

	/**
	 * 异或<br>
	 * 以null为false,exist为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 0<br>
	 * 0 1 1<br>
	 * 1 0 1<br>
	 * 1 1 0<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean xor(Object a, Object b) {
		return xor(a != null, b != null);
	}

	/**
	 * 异或非<br>
	 * 以null为false,exist为true,真值表如下:<br>
	 * 输入 输出<br>
	 * A B R<br>
	 * 0 0 1<br>
	 * 0 1 0<br>
	 * 1 0 0<br>
	 * 1 1 1<br>
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static boolean xnor(Object a, Object b) {
		return !xor(a != null, b != null);
	}

}
