package info.frodez.util.result;

/**
 * 返回值工具类
 * @author Frodez
 * @date 2018-12-01
 */
public class ResultUtil {

	/**
	 * 失败返回值
	 */
	private static final String FAIL_STRING = "{\"status\":" + ResultEnum.FAIL.getValue()
		+ ",\"message\":\"" + ResultEnum.FAIL.getDescription() + "\",\"data\":null}";

	/**
	 * 用户未登录返回值
	 */
	private static final String NOT_LOGIN_STRING = "{\"status\":" + ResultEnum.NOT_LOGIN.getValue()
		+ ",\"message\":\"" + ResultEnum.NOT_LOGIN.getDescription() + "\",\"data\":null}";

	/**
	 * 未通过验证返回值
	 */
	private static final String NO_AUTH_STRING = "{\"status\":" + ResultEnum.NO_AUTH.getValue()
		+ ",\"message\":\"" + ResultEnum.NO_AUTH.getDescription() + "\",\"data\":null}";

	/**
	 * 缺少操作权限返回值
	 */
	private static final String NO_ACCESS_STRING = "{\"status\":" + ResultEnum.NO_ACCESS.getValue()
		+ ",\"message\":\"" + ResultEnum.NO_ACCESS.getDescription() + "\",\"data\":null}";

	/**
	 * 重复请求返回值
	 */
	private static final String REPEAT_REQUEST_STRING =
		"{\"status\":" + ResultEnum.REPEAT_REQUEST.getValue() + ",\"message\":\""
			+ ResultEnum.REPEAT_REQUEST.getDescription() + "\",\"data\":null}";

	/**
	 * 获取失败状态的默认json字符串
	 * @author Frodez
	 * @return String
	 * @date 2018-11-27
	 */
	public static String getFailString() {
		return FAIL_STRING;
	}

	/**
	 * 获取用户未登录状态的默认json字符串
	 * @author Frodez
	 * @return String
	 * @date 2018-11-27
	 */
	public static String getNotLoginString() {
		return NOT_LOGIN_STRING;
	}

	/**
	 * 获取未通过验证状态的默认json字符串
	 * @author Frodez
	 * @return String
	 * @date 2018-11-27
	 */
	public static String getNoAuthString() {
		return NO_AUTH_STRING;
	}

	/**
	 * 获取缺少操作权限状态的默认json字符串
	 * @author Frodez
	 * @return String
	 * @date 2018-11-27
	 */
	public static String getNoAccessString() {
		return NO_ACCESS_STRING;
	}

	/**
	 * 获取重复请求状态的默认json字符串
	 * @author Frodez
	 * @return String
	 * @date 2018-11-27
	 */
	public static String getRepeatRequestString() {
		return REPEAT_REQUEST_STRING;
	}

}
