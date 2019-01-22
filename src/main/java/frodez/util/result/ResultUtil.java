package frodez.util.result;

import java.util.Collection;

/**
 * 返回值工具类
 * @author Frodez
 * @date 2018-12-01
 */
public class ResultUtil {

	/**
	 * 失败返回值
	 */
	public static final String FAIL_STRING = "{\"status\":" + ResultEnum.FAIL.getValue() + ",\"message\":\""
		+ ResultEnum.FAIL.getDescription() + "\",\"data\":null}";

	/**
	 * 用户未登录返回值
	 */
	public static final String NOT_LOGIN_STRING = "{\"status\":" + ResultEnum.NOT_LOGIN.getValue() + ",\"message\":\""
		+ ResultEnum.NOT_LOGIN.getDescription() + "\",\"data\":null}";

	/**
	 * 未通过验证返回值
	 */
	public static final String NO_AUTH_STRING = "{\"status\":" + ResultEnum.NO_AUTH.getValue() + ",\"message\":\""
		+ ResultEnum.NO_AUTH.getDescription() + "\",\"data\":null}";

	/**
	 * 缺少操作权限返回值
	 */
	public static final String NO_ACCESS_STRING = "{\"status\":" + ResultEnum.NO_ACCESS.getValue() + ",\"message\":\""
		+ ResultEnum.NO_ACCESS.getDescription() + "\",\"data\":null}";

	/**
	 * 重复请求返回值
	 */
	public static final String REPEAT_REQUEST_STRING = "{\"status\":" + ResultEnum.REPEAT_REQUEST.getValue()
		+ ",\"message\":\"" + ResultEnum.REPEAT_REQUEST.getDescription() + "\",\"data\":null}";

	/**
	 * 返回分页查询类型结果(仅在成功时使用)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(int total, Collection<T> data) {
		return new Result(ResultEnum.SUCCESS, new PageVO<>(total, data));
	}

	/**
	 * 返回成功结果
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result success(Object data) {
		return new Result(ResultEnum.SUCCESS, data);
	}

	/**
	 * 返回失败结果(无信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result fail() {
		return new Result(ResultEnum.FAIL);
	}

	/**
	 * 返回失败结果(有信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result fail(String message) {
		return new Result(message, ResultEnum.FAIL);
	}

}
