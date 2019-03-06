package frodez.util.beans.result;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import frodez.config.error.exception.ParseException;
import frodez.util.json.JSONUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/**
 * 通用返回参数<br>
 * @author Frodez
 * @date 2018-11-13
 */
@EqualsAndHashCode
public class Result implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 是否为默认类型实例
	 */
	private boolean isDefault = false;

	/**
	 * 默认类型实例字符串缓存
	 */
	private static final Map<Integer, String> DEFAULT_STRING_CACHE = new HashMap<>();

	static {
		for (ResultEnum item : ResultEnum.values()) {
			DEFAULT_STRING_CACHE.put(item.val, JSONUtil.string(new Result(item)));
		}
	}

	/**
	 * 状态
	 */
	@Getter
	private int code;

	/**
	 * 消息
	 */
	@Getter
	private String message;

	/**
	 * 数据
	 */
	@Getter
	private Object data;

	/**
	 * 获取result的json字符串,如果存在异常,返回null
	 * @author Frodez
	 * @date 2018-11-27
	 */
	@Override
	public String toString() {
		if (isDefault) {
			return DEFAULT_STRING_CACHE.get(code);
		}
		return JSONUtil.string(this);
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
	 * 返回分页查询类型结果(仅在成功时使用)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(Page<T> page) {
		Assert.notNull(page, "数据不能为空!");
		return new Result(ResultEnum.SUCCESS, new PageVO<>(page.getTotal(), page.getPageNum(), page.getPageSize(), page
			.getResult()));
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(PageInfo<T> page) {
		Assert.notNull(page, "数据不能为空!");
		return new Result(ResultEnum.SUCCESS, new PageVO<>(page.getTotal(), page.getPageNum(), page.getPageSize(), page
			.getList()));
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
	 * 返回请求参数错误(无信息)
	 * @author Frodez
	 * @date 2019-02-02
	 */
	public static Result errorRequest() {
		return new Result(ResultEnum.ERROR_REQUEST);
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
	 * 返回未登录结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result notLogin() {
		return new Result(ResultEnum.NOT_LOGIN);
	}

	/**
	 * 返回已过期结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result expired() {
		return new Result(ResultEnum.EXPIRED);
	}

	/**
	 * 返回未通过验证结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result noAuth() {
		return new Result(ResultEnum.NO_AUTH);
	}

	/**
	 * 返回缺少操作权限结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result noAccess() {
		return new Result(ResultEnum.NO_ACCESS);
	}

	/**
	 * 返回重复请求结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result repeatRequest() {
		return new Result(ResultEnum.REPEAT_REQUEST);
	}

	private Result() {

	}

	private Result(String message, ResultEnum status) {
		this.message = message;
		this.code = status.getVal();
	}

	private Result(ResultEnum status, Object data) {
		this.message = status.getDesc();
		this.code = status.getVal();
		this.data = data;
	}

	private Result(ResultEnum status) {
		this.message = status.getDesc();
		this.code = status.getVal();
		this.isDefault = true;
	}

	/**
	 * 根据类型获取数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	public <T> T as(Class<T> klass) throws ClassCastException, ParseException {
		Assert.notNull(klass, "类型不能为空!");
		check();
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return klass.cast(data);
	}

	/**
	 * 根据类型获取分页类型数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> PageVO<T> page(Class<T> klass) throws ClassCastException, ParseException {
		Assert.notNull(klass, "类型不能为空!");
		check();
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (PageVO<T>) data;
	}

	/**
	 * 根据类型获取集合数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> list(Class<T> klass) throws ClassCastException, ParseException {
		Assert.notNull(klass, "类型不能为空!");
		check();
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (List<T>) data;
	}

	/**
	 * 根据类型获取集合数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> Set<T> set(Class<T> klass) throws ClassCastException, ParseException {
		Assert.notNull(klass, "类型不能为空!");
		check();
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (Set<T>) data;
	}

	/**
	 * 根据类型获取集合数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> map() throws ClassCastException, ParseException {
		check();
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (Map<String, Object>) data;
	}

	/**
	 * 根据类型获取集合数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> map(Class<K> keyClass, Class<V> valueClass) throws ClassCastException, ParseException {
		Assert.notNull(keyClass, "键类型不能为空!");
		Assert.notNull(valueClass, "值类型不能为空!");
		check();
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (Map<K, V>) data;
	}

	/**
	 * 判断是否成功,不成功抛出ParseException
	 * @author Frodez
	 * @date 2019-02-13
	 */
	public void check() throws ParseException {
		if (code != ResultEnum.SUCCESS.getVal()) {
			throw new ParseException(message);
		}
	}

	/**
	 * 判断是否可用,true:不可用 false:可用<br>
	 * <strong>常用例子:</strong>
	 *
	 * <pre>
	 * if (result.unable()) {
	 * 	return result;
	 * }
	 * </pre>
	 *
	 * @author Frodez
	 * @date 2018-11-13
	 */
	public boolean unable() {
		return code != ResultEnum.SUCCESS.getVal();
	}

	/**
	 * 获取对应的HttpStatus
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public HttpStatus httpStatus() {
		return ResultEnum.of(code).getStatus();
	}

	/**
	 * 返回状态枚举
	 * @author Frodez
	 * @date 2018-11-13
	 */
	@Getter
	@AllArgsConstructor
	public enum ResultEnum implements Serializable {

		/**
		 * 操作成功,与预期相符
		 */
		SUCCESS(1000, HttpStatus.OK, "成功"),
		/**
		 * 操作失败,与预期不符
		 */
		FAIL(1001, HttpStatus.OK, "失败"),
		/**
		 * 请求参数错误
		 */
		ERROR_REQUEST(1002, HttpStatus.BAD_REQUEST, "请求参数错误"),
		/**
		 * 服务器错误
		 */
		ERROR_SERVICE(1003, HttpStatus.INTERNAL_SERVER_ERROR, "服务器错误"),
		/**
		 * 未登录
		 */
		NOT_LOGIN(2001, HttpStatus.UNAUTHORIZED, "未登录"),
		/**
		 * 已过期
		 */
		EXPIRED(2002, HttpStatus.UNAUTHORIZED, "已过期"),
		/**
		 * 未通过验证
		 */
		NO_AUTH(2003, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "未通过验证"),

		/**
		 * 缺少操作权限
		 */
		NO_ACCESS(2004, HttpStatus.FORBIDDEN, "无权限"),
		/**
		 * 重复请求
		 */
		REPEAT_REQUEST(2005, HttpStatus.LOCKED, "重复请求");

		/**
		 * 自定义状态码
		 */
		private int val;

		/**
		 * http状态码
		 */
		private HttpStatus status;

		/**
		 * 描述
		 */
		private String desc;

		private static final Map<Integer, ResultEnum> enumMap;

		static {
			enumMap = new HashMap<>();
			for (ResultEnum iter : ResultEnum.values()) {
				enumMap.put(iter.val, iter);
			}
		}

		public static ResultEnum of(int value) {
			return enumMap.get(value);
		}

	}

}
