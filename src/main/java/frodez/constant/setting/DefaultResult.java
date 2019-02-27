package frodez.constant.setting;

import frodez.util.result.Result;
import frodez.util.result.ResultEnum;

public class DefaultResult {

	/**
	 * 失败返回值
	 */
	public static final String FAIL_STRING = new Result(ResultEnum.FAIL).toString();

	/**
	 * 参数错误返回值
	 */
	public static final String ERROR_REQUEST_STRING = new Result(ResultEnum.ERROR_REQUEST).toString();

	/**
	 * 失败返回值
	 */
	public static final String ERROR_SERVICE_STRING = new Result(ResultEnum.ERROR_SERVICE).toString();

	/**
	 * 未登录返回值
	 */
	public static final String NOT_LOGIN_STRING = new Result(ResultEnum.NOT_LOGIN).toString();

	/**
	 * 已过期返回值
	 */
	public static final String EXPIRED_STRING = new Result(ResultEnum.EXPIRED).toString();

	/**
	 * 未通过验证返回值
	 */
	public static final String NO_AUTH_STRING = new Result(ResultEnum.NO_AUTH).toString();

	/**
	 * 缺少操作权限返回值
	 */
	public static final String NO_ACCESS_STRING = new Result(ResultEnum.NO_ACCESS).toString();

	/**
	 * 重复请求返回值
	 */
	public static final String REPEAT_REQUEST_STRING = new Result(ResultEnum.REPEAT_REQUEST).toString();

}
