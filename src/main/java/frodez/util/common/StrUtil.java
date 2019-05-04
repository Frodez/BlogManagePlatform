package frodez.util.common;

import frodez.util.constant.setting.DefDecimal;
import java.math.BigDecimal;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

/**
 * 字符串处理工具类
 * @author Frodez
 * @date 2019-03-27
 */
@UtilityClass
public class StrUtil {

	/**
	 * 将一个对象转化为string<br>
	 * 本方法做如下转换:<br>
	 * 1.如果对象为null,则转换为空字符串。<br>
	 * 2.如果对象为BigDecimal,则按规定的默认精度和转换方式转换为字符串。<br>
	 * 3.如果对象不为BigDecimal,则调用其默认的toString方法转换为字符串。<br>
	 * @see frodez.util.constant.setting.DefDecimal
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String get(@Nullable Object object) {
		return get("", object);
	}

	/**
	 * 将一个对象转化为string<br>
	 * 本方法做如下转换:<br>
	 * 1.如果对象为null,则转换为defaultStr。如果defaultStr也为null,则抛出IllegalArgumentException。<br>
	 * 2.如果对象为BigDecimal,则按规定的默认精度和转换方式转换为字符串。<br>
	 * 3.如果对象不为BigDecimal,则调用其默认的toString方法转换为字符串。<br>
	 * @see frodez.util.constant.setting.DefDecimal
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String get(String defaultStr, @Nullable Object object) {
		if (object == null) {
			if (defaultStr == null) {
				throw new IllegalArgumentException();
			}
			return defaultStr;
		}
		if (object.getClass() == BigDecimal.class) {
			return ((BigDecimal) object).setScale(DefDecimal.PRECISION, DefDecimal.ROUND_MODE).toString();
		}
		return object.toString();
	}

	/**
	 * 批量拼接字符串,对null会当作空字符串处理。<br>
	 * 在极端的情况下,会直接返回原字符串(例如只有一个字符串传入且该字符串不为null)。这种情况可以极大地加快速度。<br>
	 * 当然,如果只有一个字符串且该字符串为null,则会直接返回默认字符串,同样可以极大地加快速度。<br>
	 * 另外由于String类型是inmutable的,故只要不涉及对其内存地址的操作,则不会出现bug。<br>
	 * 经测试,在绝大多数场景下相对jdk的实现更快(平均40%左右),在最坏情况下也与其相当。
	 * @see java.lang.String#concat(String)
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String concat(String... strings) {
		return concatWithDefault("", strings);
	}

	/**
	 * 批量拼接字符串,将null处理为默认字符串。默认字符串可以为空字符串,但不能为null。<br>
	 * 在极端的情况下,会直接返回原字符串(例如只有一个字符串传入且该字符串不为null)。这种情况可以极大地加快速度。<br>
	 * 当然,如果只有一个字符串且该字符串为null,则会直接返回默认字符串,同样可以极大地加快速度。<br>
	 * 另外由于String类型是inmutable的,故只要不涉及对其内存地址的操作,则不会出现bug。<br>
	 * 经测试,在绝大多数场景下相对jdk的实现更快(平均40%左右),在最坏情况下也与其相当。
	 * @param defaultStr 为null时的默认字符串
	 * @see java.lang.String#concat(String)
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String concatWithDefault(String defaultStr, String... strings) {
		if (defaultStr == null || EmptyUtil.yes(strings)) {
			throw new IllegalArgumentException();
		}
		int stringsLength = strings.length;
		if (stringsLength == 1) {
			return strings[0] == null ? defaultStr : strings[0];
		}
		int size = 0;
		int defaultStrLength = defaultStr.length();
		for (int i = 0; i < stringsLength; i++) {
			size = size + (strings[i] == null ? defaultStrLength : strings[i].length());
		}
		StringBuilder builder = new StringBuilder(size);
		for (int i = 0; i < stringsLength; i++) {
			builder.append(strings[i] == null ? defaultStr : strings[i]);
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
			throw new IllegalArgumentException();
		}
		return new StringBuilder(string.length()).append(Character.toUpperCase(string.charAt(0))).append(string
			.substring(1)).toString();
	}

	/**
	 * 首字母小写
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static String lowerFirst(String string) {
		if (EmptyUtil.yes(string)) {
			throw new IllegalArgumentException();
		}
		return new StringBuilder(string.length()).append(Character.toLowerCase(string.charAt(0))).append(string
			.substring(1)).toString();
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
		if (string == null || EmptyUtil.yes(delimiter)) {
			throw new IllegalArgumentException();
		}
		String[] tokens = string.split(delimiter);
		int tokensLength = tokens.length;
		if (tokensLength <= 1) {
			return new StringBuilder(string.length()).append(Character.toLowerCase(string.charAt(0))).append(string
				.substring(1)).toString();
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

}
