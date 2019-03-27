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

	public static String get(@Nullable Object object) {
		return get(object, "");
	}

	public static String get(@Nullable Object object, String defaultStr) {
		if (object == null) {
			return defaultStr;
		}
		if (object.getClass() == BigDecimal.class) {
			return ((BigDecimal) object).setScale(DefDecimal.PRECISION, DefDecimal.ROUND_MODE).toString();
		}
		return object.toString();
	}

	public static String concat(String... strings) {
		if (EmptyUtil.yes(strings)) {
			throw new IllegalArgumentException();
		}
		StringBuilder builder = new StringBuilder();
		for (String string : strings) {
			builder.append(string);
		}
		return builder.toString();
	}

}
