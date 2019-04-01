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
	public static String get(@Nullable Object object, String defaultStr) {
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
	 * 批量拼接字符串<br>
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String concat(String... strings) {
		if (EmptyUtil.yes(strings)) {
			throw new IllegalArgumentException();
		}
		int size = 0;
		for (String string : strings) {
			size = size + string.length();
		}
		StringBuilder builder = new StringBuilder(size);
		for (String string : strings) {
			builder.append(string);
		}
		return builder.toString();
	}

}
