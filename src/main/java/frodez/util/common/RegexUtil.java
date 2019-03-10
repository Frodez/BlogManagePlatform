package frodez.util.common;

import frodez.util.constant.setting.DefStr;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.validation.constraints.Pattern.Flag;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexUtil {

	private static final Map<String, Pattern> CACHE = new ConcurrentHashMap<>();

	/**
	 * 是否通过正则表达式校验
	 * @author Frodez
	 * @date 2019-03-05
	 */
	public static boolean match(String regex, String input) {
		return CACHE.computeIfAbsent(regex, i -> Pattern.compile(i)).matcher(input).matches();
	}

	/**
	 * 是否通过正则表达式校验
	 * @author Frodez
	 * @date 2019-03-05
	 */
	public static boolean match(String regex, String input, int flag) {
		if (flag == 0) {
			return CACHE.computeIfAbsent(regex, i -> Pattern.compile(i)).matcher(input).matches();
		}
		return CACHE.computeIfAbsent(regex.concat(DefStr.SEPERATOR).concat(Integer.toString(flag)), i -> Pattern
			.compile(i)).matcher(input).matches();
	}

	/**
	 * 是否通过正则表达式校验
	 * @author Frodez
	 * @date 2019-03-05
	 */
	public static boolean match(String regex, String input, Flag... flags) {
		return match(regex, input, transfer(flags));
	}

	/**
	 * 转换枚举
	 * @author Frodez
	 * @date 2019-03-05
	 */
	public static int transfer(Flag... flags) {
		int flag = 0;
		for (Flag item : flags) {
			flag = flag | item.getValue();
		}
		return flag;
	}

}
