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
		return get(object, "");
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
	public static String get(@Nullable Object object, CharSequence defaultStr) {
		if (object == null) {
			if (defaultStr == null) {
				throw new IllegalArgumentException();
			}
			return defaultStr.toString();
		}
		if (object.getClass() == BigDecimal.class) {
			return ((BigDecimal) object).setScale(DefDecimal.PRECISION, DefDecimal.ROUND_MODE).toString();
		}
		return object.toString();
	}

	/**
	 * 批量拼接字符串,对null会当作空字符串处理。
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String concat(CharSequence... strings) {
		return concat("", strings);
	}

	/**
	 * 批量拼接字符串,将null处理为默认字符串。默认字符串可以为空字符串,但不能为null。
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String concat(CharSequence defaultStr, CharSequence... strings) {
		if (defaultStr == null || EmptyUtil.yes(false, strings)) {
			throw new IllegalArgumentException();
		}
		int size = 0;
		for (CharSequence string : strings) {
			size = size + (string == null ? defaultStr.length() : string.length());
		}
		StringBuilder builder = new StringBuilder(size);
		for (CharSequence string : strings) {
			if (string == null) {
				builder.append(defaultStr);
			} else {
				builder.append(string);
			}
		}
		return builder.toString();
	}

	/**
	 * 批量连接字符串,中间有分隔符,可自定义字符串为null时的替代字符串。<br>
	 * 不需要自定义null的替代字符串时,建议使用String.join方法<br>
	 * <strong>替代字符串可以为空字符串,但不能为null。但是分隔符不能为null或者空字符串!!!</strong>
	 * @see java.lang.String#join(CharSequence, CharSequence...)
	 * @author Frodez
	 * @date 2019-04-02
	 */
	public static String join(CharSequence defaultStr, CharSequence delimiter, CharSequence... strings) {
		if (defaultStr == null || EmptyUtil.yes(delimiter) || EmptyUtil.yes(false, strings)) {
			throw new IllegalArgumentException();
		}
		int size = (strings.length - 1) * delimiter.length();
		for (CharSequence string : strings) {
			size = size + (string == null ? defaultStr.length() : string.length());
		}
		StringBuilder builder = new StringBuilder(size);
		builder.append(strings[0]);
		for (int i = 1; i < strings.length; i++) {
			builder.append(delimiter);
			if (strings[i] == null) {
				builder.append(defaultStr);
			} else {
				builder.append(strings[i]);
			}
		}
		return builder.toString();
	}

}
