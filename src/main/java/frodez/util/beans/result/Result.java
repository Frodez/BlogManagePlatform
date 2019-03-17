package frodez.util.beans.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import frodez.util.constant.setting.DefDesc;
import frodez.util.error.exception.ParseException;
import frodez.util.json.JSONUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.EnumMap;
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
@ApiModel(description = DefDesc.Message.RESULT)
public final class Result implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 默认json,只有默认类型实例才存在
	 */
	private String json;

	/**
	 * 默认类型实例
	 */
	private static final Map<ResultEnum, Result> DEFAULT_RESULT_CACHE = new EnumMap<>(ResultEnum.class);

	/**
	 * jackson writer
	 */
	private static ObjectWriter writer;

	static {
		writer = JSONUtil.mapper().writerFor(Result.class);
		for (ResultEnum item : ResultEnum.values()) {
			Result result = new Result(item);
			try {
				result.json = writer.writeValueAsString(result);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			DEFAULT_RESULT_CACHE.put(item, result);
		}
	}

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态", example = "1000")
	private int code;

	/**
	 * 获取状态
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 消息
	 */
	@ApiModelProperty(value = "消息", example = "成功")
	private String message;

	/**
	 * 获取消息
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 数据
	 */
	@ApiModelProperty(value = "数据")
	private Object data;

	/**
	 * <strong>此方法仅用于json解析,其他时候不得使用!!!</strong>
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 获取result的json字符串,如果存在异常,返回null
	 * @author Frodez
	 * @date 2018-11-27
	 */
	@Override
	public String toString() {
		if (json != null) {
			return json;
		}
		try {
			return writer.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 返回成功结果(无数据)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result success() {
		return DEFAULT_RESULT_CACHE.get(ResultEnum.SUCCESS);
	}

	/**
	 * 返回成功结果(有数据)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result success(Object data) {
		Assert.notNull(data, "data must not be null");
		return new Result(ResultEnum.SUCCESS, data);
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(Page<T> page) {
		Assert.notNull(page, "page must not be null");
		return new Result(ResultEnum.SUCCESS, new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(),
			page.getResult()));
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(PageInfo<T> page) {
		Assert.notNull(page, "page must not be null");
		return new Result(ResultEnum.SUCCESS, new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(),
			page.getList()));
	}

	/**
	 * 返回失败结果(无信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result fail() {
		return DEFAULT_RESULT_CACHE.get(ResultEnum.FAIL);
	}

	/**
	 * 返回失败结果(有信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result fail(String message) {
		Assert.notNull(message, "message must not be null");
		return new Result(message, ResultEnum.FAIL);
	}

	/**
	 * 返回请求参数错误(无信息)
	 * @author Frodez
	 * @date 2019-02-02
	 */
	public static Result errorRequest() {
		return DEFAULT_RESULT_CACHE.get(ResultEnum.ERROR_REQUEST);
	}

	/**
	 * 返回请求参数错误(有信息)
	 * @author Frodez
	 * @date 2019-02-02
	 */
	public static Result errorRequest(String message) {
		Assert.notNull(message, "message must not be null");
		return new Result(message, ResultEnum.ERROR_REQUEST);
	}

	/**
	 * 返回服务器错误结果(无信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result errorService() {
		return DEFAULT_RESULT_CACHE.get(ResultEnum.ERROR_SERVICE);
	}

	/**
	 * 返回服务器错误结果(有信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result errorService(String message) {
		Assert.notNull(message, "message must not be null");
		return new Result(message, ResultEnum.ERROR_SERVICE);
	}

	/**
	 * 返回未登录结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result notLogin() {
		return DEFAULT_RESULT_CACHE.get(ResultEnum.NOT_LOGIN);
	}

	/**
	 * 返回已过期结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result expired() {
		return DEFAULT_RESULT_CACHE.get(ResultEnum.EXPIRED);
	}

	/**
	 * 返回未通过验证结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result noAuth() {
		return DEFAULT_RESULT_CACHE.get(ResultEnum.NO_AUTH);
	}

	/**
	 * 返回缺少操作权限结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result noAccess() {
		return DEFAULT_RESULT_CACHE.get(ResultEnum.NO_ACCESS);
	}

	/**
	 * 返回重复请求结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result repeatRequest() {
		return DEFAULT_RESULT_CACHE.get(ResultEnum.REPEAT_REQUEST);
	}

	/**
	 * 返回服务器繁忙结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result busy() {
		return DEFAULT_RESULT_CACHE.get(ResultEnum.BUSY);
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
	}

	/**
	 * 根据类型获取数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	public <T> T as(Class<T> klass) throws ClassCastException, ParseException {
		Assert.notNull(klass, "klass must not be null");
		ableAndNotNull();
		return klass.cast(data);
	}

	/**
	 * 根据类型获取分页类型数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> PageData<T> page(Class<T> klass) throws ClassCastException, ParseException {
		Assert.notNull(klass, "klass must not be null");
		ableAndNotNull();
		return (PageData<T>) data;
	}

	/**
	 * 根据类型获取集合数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> list(Class<T> klass) throws ClassCastException, ParseException {
		Assert.notNull(klass, "klass must not be null");
		ableAndNotNull();
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
		Assert.notNull(klass, "klass must not be null");
		ableAndNotNull();
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
		ableAndNotNull();
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
		Assert.notNull(keyClass, "keyClass must not be null");
		Assert.notNull(valueClass, "valueClass must not be null");
		ableAndNotNull();
		return (Map<K, V>) data;
	}

	/**
	 * 获取数据的类型
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public Class<?> dataType() {
		ableAndNotNull();
		return data.getClass();
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
	 * <pre>
	 * if (result.unable()) {
	 * 	throw new RuntimeException();
	 * }
	 * </pre>
	 *
	 * @author Frodez
	 * @date 2018-11-13
	 */
	public boolean unable() {
		return code != ResultEnum.SUCCESS.val;
	}

	/**
	 * 获取对应的HttpStatus
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public HttpStatus httpStatus() {
		return ResultEnum.of(code).status;
	}

	private void ableAndNotNull() {
		if (code != ResultEnum.SUCCESS.val) {
			throw new UnsupportedOperationException(message);
		}
		if (data == null) {
			throw new ParseException("数据为空!");
		}
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
		REPEAT_REQUEST(2005, HttpStatus.LOCKED, "重复请求"),
		/**
		 * 服务器繁忙
		 */
		BUSY(2006, HttpStatus.SERVICE_UNAVAILABLE, "服务器繁忙");

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
