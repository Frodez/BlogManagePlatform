package frodez.util.common;

import frodez.constant.settings.DefStr;
import java.lang.reflect.Method;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import lombok.experimental.UtilityClass;

/**
 * 字符串处理工具类
 * @author Frodez
 * @date 2019-03-27
 */
@UtilityClass
public class StrUtil {

	private static byte code = 0;

	private static final CharsetEncoder ASCII_ENCODER = StandardCharsets.US_ASCII.newEncoder();

	static {
		try {
			Method method = String.class.getDeclaredMethod("coder", new Class<?>[0]);
			method.setAccessible(true);
			code = (byte) method.invoke(new String());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}

	/**
	 * 将空字符串转换为null
	 * @author Frodez
	 * @date 2019-12-07
	 */
	public static String orNull(String string) {
		if (string == null) {
			return null;
		}
		return string.isEmpty() ? null : string;
	}

	/**
	 * 将null转换为空字符串
	 * @author Frodez
	 * @date 2019-12-07
	 */
	public static String orEmpty(String string) {
		return string == null ? DefStr.EMPTY : string;
	}

	/**
	 * 批量拼接字符串。<br>
	 * 注意:<strong>当输入的字符串数组中某处为null时，会抛出异常.</strong><br>
	 * 经测试,在绝大多数场景下相对jdk的实现更快(平均20-30%左右),在最坏情况下也与其相当。<br>
	 * 与StringBuilder的非优化使用方式相比,性能提高从20%-70%不等。当拼接的字符串长度较长,或者字符串数组长度较长时,性能优势更大。
	 * @see java.lang.String#concat(String)
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String concat(String... strings) {
		if (EmptyUtil.yes(strings)) {
			throw new IllegalArgumentException("it isn't suitable for empty string.");
		}
		int size = 0;
		for (int i = 0; i < strings.length; i++) {
			//如果字符串数组某处为null,会自动抛出异常
			size = size + strings[i].length();
		}
		size = size << code;
		StringBuilder builder = new StringBuilder(size);
		for (int i = 0; i < strings.length; i++) {
			builder.append(strings[i]);
		}
		return builder.toString();
	}

	/**
	 * 批量拼接字符串。<br>
	 * 注意:<strong>当输入的字符串数组中某处为null时，会抛出异常.</strong><br>
	 * 经测试,在绝大多数场景下相对jdk的实现更快(平均20-30%左右),在最坏情况下也与其相当。<br>
	 * 与StringBuilder的非优化使用方式相比,性能提高从20%-70%不等。当拼接的字符串长度较长,或者字符串数组长度较长时,性能优势更大。
	 * @see java.lang.String#concat(String)
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String concat(Collection<String> strings) {
		if (EmptyUtil.yes(strings)) {
			throw new IllegalArgumentException("it isn't suitable for empty string.");
		}
		int size = 0;
		for (String string : strings) {
			//如果字符串数组某处为null,会自动抛出异常
			size = size + string.length();
		}
		size = size << code;
		StringBuilder builder = new StringBuilder(size);
		for (String string : strings) {
			builder.append(string);
		}
		return builder.toString();
	}

	/**
	 * 首字母大写
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static String upperFirst(String string) {
		if (EmptyUtil.yes(string)) {
			throw new IllegalArgumentException("it isn't suitable for empty string.");
		}
		return new StringBuilder(string.length()).append(Character.toUpperCase(string.charAt(0))).append(string.substring(1)).toString();
	}

	/**
	 * 首字母小写
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static String lowerFirst(String string) {
		if (EmptyUtil.yes(string)) {
			throw new IllegalArgumentException("it isn't suitable for empty string.");
		}
		return new StringBuilder(string.length()).append(Character.toLowerCase(string.charAt(0))).append(string.substring(1)).toString();
	}

	/**
	 * 转换为驼峰命名法<br>
	 * 默认以"-"为分隔符,将首字母变为小写,将之后的每个分词的首字母变为大写。如果分隔符无效,则会将首字母变为小写。<br>
	 * @author Frodez
	 * @date 2019-04-17
	 */
	public static String toCamelCase(String string) {
		return toCamelCase("-", string);
	}

	/**
	 * 转换为驼峰命名法<br>
	 * 以设定的分隔符作为标准,将首字母变为小写,将之后的每个分词的首字母变为大写。如果分隔符无效,则会将首字母变为小写。<br>
	 * @author Frodez
	 * @date 2019-04-17
	 */
	public static String toCamelCase(String delimiter, String string) {
		if (string == null || delimiter == null) {
			throw new IllegalArgumentException("when string is null, delimiter can't be null either.");
		}
		String[] tokens = string.split(delimiter);
		int tokensLength = tokens.length;
		if (tokensLength <= 1) {
			return new StringBuilder(string.length()).append(Character.toLowerCase(string.charAt(0))).append(string.substring(1)).toString();
		}
		char[] upperStarters = new char[tokensLength - 1];
		for (int i = 1; i < tokensLength; i++) {
			upperStarters[i - 1] = Character.toUpperCase(tokens[i].charAt(0));
			tokens[i] = tokens[i].substring(1);
		}
		StringBuilder builder = new StringBuilder(string.length());
		builder.append(Character.toLowerCase(tokens[0].charAt(0)));
		builder.append(tokens[0].substring(1));
		for (int i = 1; i < tokensLength; i++) {
			builder.append(upperStarters[i]).append(tokens[i]);
		}
		return builder.toString();
	}

	/**
	 * 是否为ascii字符串
	 * @author Frodez
	 * @date 2019-12-07
	 */
	public boolean isAscii(String string) {
		return ASCII_ENCODER.canEncode(string);
	}

}
