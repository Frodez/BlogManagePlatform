package frodez.constant.settings;

import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/**
 * 默认正则表达式设置
 * @author Frodez
 * @date 2019-03-27
 */
@UtilityClass
public class DefRegex {

	/**
	 * 合法手机号
	 */
	public static final String MOBILE_REGEX =
		"/^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$/";

	/**
	 * 合法手机号
	 */
	public static final Pattern MOBILE = Pattern.compile(MOBILE_REGEX);

	/**
	 * 合法身份证
	 */
	public static final String IDCARD_REGEX =
		"/^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$/";

	/**
	 * 合法身份证
	 */
	public static final Pattern IDCARD = Pattern.compile(IDCARD_REGEX);

}
