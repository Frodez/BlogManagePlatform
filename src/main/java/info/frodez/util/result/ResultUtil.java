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
	public static final int SUCCESS_VALUE = 1000;

	/**
	 * 失败返回值
	 */
	public static final int FAIL_VALUE = 1001;

	/**
	 * 用户未登录返回值
	 */
	public static final int NOT_LOGIN_VALUE = 2001;

	/**
	 * 未通过验证返回值
	 */
	public static final int NO_AUTH_VALUE = 2002;

	/**
	 * 缺少操作权限返回值
	 */
	public static final int NO_ACCESS_VALUE = 2003;

	/**
	 * 重复请求返回值
	 */
	public static final int REPEAT_REQUEST_VALUE = 2004;

	/**
	 * 获取失败状态的json字符串
	 * @author Frodez
	 * @return String
	 * @date 2018-11-27
	 */
	public static String getFailString() {
		return "{\"status\":" + FAIL_VALUE + ",\"message\":\"失败\",\"data\":null}";
	}

}
