package info.frodez.util.result;

/**
 * 返回值工具类
 * @author Frodez
 * @date 2018-12-01
 */
public class ResultUtil {

	/**
	 * 成功返回值
	 */
	public static final byte SUCCESS_VALUE = (byte) 1000;

	/**
	 * 失败返回值
	 */
	public static final byte FAIL_VALUE = (byte) 1001;
	
	/**
	 * 失败返回值
	 */
	public static final String FAIL_STRING = "{\"status\":" + FAIL_VALUE + ",\"message\":\"" + 
		ResultEnum.FAIL.getDescription() + "\",\"data\":null}";

	/**
	 * 用户未登录返回值
	 */
	public static final byte NOT_LOGIN_VALUE = (byte) 2001;
	
	/**
	 * 用户未登录返回值
	 */
	public static final String NOT_LOGIN_STRING = "{\"status\":" + NOT_LOGIN_VALUE + ",\"message\":\"" + 
		ResultEnum.NOT_LOGIN.getDescription() + "\",\"data\":null}";

	/**
	 * 未通过验证返回值
	 */
	public static final byte NO_AUTH_VALUE = (byte) 2002;
	
	/**
	 * 未通过验证返回值
	 */
	public static final String NO_AUTH_STRING = "{\"status\":" + NO_AUTH_VALUE + ",\"message\":\"" + 
		ResultEnum.NO_AUTH.getDescription() + "\",\"data\":null}";

	/**
	 * 缺少操作权限返回值
	 */
	public static final byte NO_ACCESS_VALUE = (byte) 2003;
	
	/**
	 * 缺少操作权限返回值
	 */
	public static final String NO_ACCESS_STRING = "{\"status\":" + NO_ACCESS_VALUE + ",\"message\":\"" + 
		ResultEnum.NO_ACCESS.getDescription() + "\",\"data\":null}";

	/**
	 * 重复请求返回值
	 */
	public static final byte REPEAT_REQUEST_VALUE = (byte) 2004;
	
	/**
	 * 重复请求返回值
	 */
	public static final String REPEAT_REQUEST_STRING = "{\"status\":" + REPEAT_REQUEST_VALUE + ",\"message\":\"" + 
		ResultEnum.REPEAT_REQUEST.getDescription() + "\",\"data\":null}";

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
