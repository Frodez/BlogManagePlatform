package frodez.util.common;

import frodez.util.constant.setting.DefDecimal;
import java.math.BigDecimal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StrUtil {

	public static String get(Object object) {
		return get(object, "");
	}

	public static String get(Object object, String defaultStr) {
		if (object == null) {
			return defaultStr;
		}
		if (object.getClass() == BigDecimal.class) {
			return BigDecimal.class.cast(object).setScale(DefDecimal.PRECISION, DefDecimal.ROUND_MODE).toString();
		}
		return object.toString();
	}

}
