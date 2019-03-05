package frodez.util.common;

import frodez.constant.setting.DefStr;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.validation.constraints.Pattern.Flag;

public class RegexUtil {

	private static final Map<String, Pattern> CACHE = new ConcurrentHashMap<>();

	private static Pattern get(String regex) {
		Pattern pattern = CACHE.get(regex);
		if (pattern != null) {
			return pattern;
		}
		pattern = Pattern.compile(regex);
		CACHE.put(regex, pattern);
		return pattern;
	}

	private static Pattern get(String regex, int flag) {
		String key = new StringBuilder(regex).append(DefStr.SEPERATOR).append(flag).toString();
		Pattern pattern = CACHE.get(key);
		if (pattern != null) {
			return pattern;
		}
		pattern = Pattern.compile(regex, flag);
		CACHE.put(key, pattern);
		return pattern;
	}

	/**
	 * 是否通过正则表达式校验
	 * @author Frodez
	 * @date 2019-03-05
	 */
	public static boolean match(String regex, String input) {
		return get(regex).matcher(input).matches();
	}

	/**
	 * 是否通过正则表达式校验
	 * @author Frodez
	 * @date 2019-03-05
	 */
	public static boolean match(String regex, String input, int flag) {
		if (flag == 0) {
			return get(regex).matcher(input).matches();
		}
		return get(regex, flag).matcher(input).matches();
	}

	/**
	 * 是否通过正则表达式校验
	 * @author Frodez
	 * @date 2019-03-05
	 */
	public static boolean match(String regex, String input, Flag... flags) {
		return match(regex, input, reverse(flags));
	}

	/**
	 * 转换枚举
	 * @author Frodez
	 * @date 2019-03-05
	 */
	public static int reverse(Flag... flags) {
		int flag = 0;
		for (Flag item : flags) {
			flag = flag | item.getValue();
		}
		return flag;
	}

}