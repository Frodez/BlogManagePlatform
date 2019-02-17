package frodez.util.common;

import frodez.constant.setting.DefaultDecimal;
import java.math.BigDecimal;

public class StrUtil {

	public static String get(Object object) {
		return get(object, "");
	}

	public static String get(Object object, String defaultStr) {
		if (object == null) {
			return defaultStr;
		}
		if (object.getClass() == BigDecimal.class) {
			return BigDecimal.class.cast(object).setScale(DefaultDecimal.PRECISION, DefaultDecimal.ROUND_MODE)
				.toString();
		}
		return object.toString();
	}

}
