package frodez.util.common;

import frodez.constant.setting.DefaultDecimal;
import java.math.BigDecimal;
import org.springframework.util.Assert;

public class StrUtil {

	public static String str(Object object) {
		return str(object, "");
	}

	public static String str(Object object, String defaultStr) {
		if (object == null) {
			return defaultStr;
		}
		if (object.getClass() == BigDecimal.class) {
			return BigDecimal.class.cast(object).setScale(DefaultDecimal.PRECISION, DefaultDecimal.ROUND_MODE)
				.toString();
		}
		return object.toString();
	}

	public static String concat(String separator, String... args) {
		Assert.notNull(separator, "分隔符不能为空!");
		Assert.notEmpty(args, "参数不能为空!");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < args.length - 1; i++) {
			builder.append(args[i]).append(separator);
		}
		return builder.append(args[args.length - 1]).toString();
	}

}
