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
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 通用返回参数<br>
 * 使用方法:<br>
 * 1.返回数据
 *
 * <pre>
 * Result.success();//返回成功状态,但不具有数据
 * Result.success(SomeObject());//返回成功状态,具有数据
 * Result.success(Arrays.asList(SomeObject(), SomeObject(), SomeObject()));//返回成功状态,具有数据,数据使用容器包装
 * Result.fail();//返回失败状态,不具有详细信息
 * Result.fail("...");//返回失败状态,具有详细信息
 * Result.page(new Page());//返回成功状态,数据为分页查询得到的数据
 * </pre>
 *
 * 2.获取数据
 *
 * <pre>
 * 只有Result的状态为success时才允许获取数据,否则抛出异常。如果检查到数据为null,也会抛出异常。
 * 如果Result的数据类型为SomeObject,那么可以使用Result.as(Class<T>):
 * result = Result.success(SomeObject());
 * result.as(SomeObject.class);
 * 如果Result的数据类型为SomeObject的List,可使用Result.list(Class<T>):
 * result = Result.success(Arrays.asList(SomeObject(), SomeObject(), SomeObject()));
 * result.list(SomeObject.class);
 * Set和Map的获取方式同上。
 * </pre>
 *
 * 3.检查Result状态
 *
 * <pre>
 * 只有success状态才是正常状态,其他状态为非正常状态。需要判断状态时,可用Result.unable()方法检查:
 * result = Result.success(SomeObject());
 * result.unable();
 * </pre>
 *
 * 4.其他方法
 *
 * <pre>
 * toString方法被重写,使用jackson输出json,对常用的默认状态(无数据,默认信息的状态)进行了优化;
 * 涉及异步时,可使用async(),用Future包装;
 * 数据类型不确定时,可使用dataType()获得数据类型;
 * httpStatus()和resultEnum()用于获取Result的http状态码和自定义状态码。
 * </pre>
 *
 * @author Frodez
 * @date 2018-11-13
 */
@EqualsAndHashCode
@ApiModel(description = DefDesc.Message.RESULT)
public final class Result implements Serializable {

	/**
	 * 默认json,只有默认类型实例才存在
	 */
	private transient String json;

	/**
	 * 默认类型实例
	 */
	private static transient final Map<ResultEnum, Result> DEFAULT_RESULT_CACHE = new EnumMap<>(ResultEnum.class);

	/**
	 * jackson writer
	 */
	private static transient ObjectWriter writer;

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
	 * <strong>此方法用于json解析,不建议在其他时候使用!!!</strong>
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 获取result的json字符串
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
	 * 返回成功结果(有数据)<br>
	 * <strong>成功状态的默认信息不可更改,此处的参数只是数据,不是信息!!!</strong>
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result success(Object data) {
		Assert.notNull(data, "data must not be null");
		return new Result(ResultEnum.SUCCESS, data);
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)<br>
	 * result.data的类型为frodez.util.beans.result.PageData
	 * @see frodez.util.beans.result.PageData
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(int pageNum, int pageSize, long total, Collection<T> data) {
		Assert.notNull(data, "data must not be null");
		return new Result(ResultEnum.SUCCESS, new PageData<>(pageNum, pageSize, total, data));
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)<br>
	 * result.data的类型为frodez.util.beans.result.PageData
	 * @see frodez.util.beans.result.PageData
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(Page<T> page) {
		Assert.notNull(page, "page must not be null");
		Assert.notNull(page.getPageNum(), "page.getPageNum() must not be null");
		Assert.notNull(page.getPageSize(), "page.getPageSize() must not be null");
		Assert.notNull(page.getTotal(), "page.getTotal() must not be null");
		Assert.notNull(page.getResult(), "page.getResult() must not be null");
		return new Result(ResultEnum.SUCCESS, new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(),
			page.getResult()));
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)<br>
	 * result.data的类型为frodez.util.beans.result.PageData
	 * @see frodez.util.beans.result.PageData
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(PageInfo<T> page) {
		Assert.notNull(page, "page must not be null");
		Assert.notNull(page.getPageNum(), "page.getPageNum() must not be null");
		Assert.notNull(page.getPageSize(), "page.getPageSize() must not be null");
		Assert.notNull(page.getTotal(), "page.getTotal() must not be null");
		Assert.notNull(page.getList(), "page.getList() must not be null");
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
	@SuppressWarnings("unchecked")
	public <T> T as(Class<T> klass) throws ClassCastException, ParseException {
		Assert.notNull(klass, "klass must not be null");
		ableAndNotNull();
		return (T) data;
	}

	/**
	 * 根据类型获取分页类型数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> PageData<T> pageData(Class<T> klass) throws ClassCastException, ParseException {
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

	/**
	 * 获取对应的状态枚举
	 * @author Frodez
	 * @date 2019-04-17
	 */
	public ResultEnum resultEnum() {
		return ResultEnum.of(code);
	}

	/**
	 * 使用异步包装
	 * @author Frodez
	 * @date 2019-04-16
	 */
	public ListenableFuture<Result> async() {
		return new AsyncResult<>(this);
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
	public enum ResultEnum {

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

	private static final long serialVersionUID = 1L;

}
