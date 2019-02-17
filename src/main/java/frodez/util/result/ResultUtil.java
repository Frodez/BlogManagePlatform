package frodez.util.result;

import java.util.Collection;
import org.springframework.util.Assert;

/**
 * 返回值工具类
 * @author Frodez
 * @date 2018-12-01
 */
public class ResultUtil {

	/**
	 * 返回分页查询类型结果(仅在成功时使用)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(int total, Collection<T> data) {
		Assert.notNull(data, "数据不能为空!");
		return new Result(ResultEnum.SUCCESS, new PageVO<>(total, data));
	}

	/**
	 * 返回成功结果(无数据)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result success() {
		return new Result(ResultEnum.SUCCESS);
	}

	/**
	 * 返回成功结果(有数据)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result success(Object data) {
		Assert.notNull(data, "数据不能为空!");
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
		Assert.notNull(message, "信息不能为空!");
		return new Result(message, ResultEnum.FAIL);
	}

	/**
	 * 返回服务器错误结果(无信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result errorService() {
		return new Result(ResultEnum.ERROR_SERVICE);
	}

	/**
	 * 返回服务器错误结果(有信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result errorService(String message) {
		Assert.notNull(message, "信息不能为空!");
		return new Result(message, ResultEnum.ERROR_SERVICE);
	}

	/**
	 * 返回请求参数错误(有信息)
	 * @author Frodez
	 * @date 2019-02-02
	 */
	public static Result errorRequest(String message) {
		Assert.notNull(message, "信息不能为空!");
		return new Result(message, ResultEnum.ERROR_REQUEST);
	}

}
