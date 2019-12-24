package frodez.util.beans.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import frodez.constant.errors.code.ServiceException;
import frodez.util.common.StrUtil;
import frodez.util.json.JSONUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
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
 * json()方法使用jackson输出json,对常用的默认状态(无数据,默认信息的状态)进行了优化;
 * 涉及异步时,可使用async(),用Future包装;
 * 数据类型不确定时,可使用dataType()获得数据类型;
 * httpStatus()和resultEnum()用于获取Result的http状态码和自定义状态码。
 * </pre>
 *
 * <strong>此参数的泛型版本见frodez.config.swagger.plugin.DefaultSuccessResolverPlugin.SwaggerModel</strong>
 * @see frodez.config.swagger.plugin.SuccessPlugin.SwaggerModel
 * @author Frodez
 * @date 2018-11-13
 */
@EqualsAndHashCode
public final class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 默认json缓存(数组类型),只有默认类型实例才存在
	 */
	@Nullable
	private transient byte[] cacheBytes;

	/**
	 * 状态
	 */
	@NotNull
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
	@NotNull
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
	 * 返回成功结果(无数据)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result success() {
		return ResultHelper.DEFAULT_CACHE.get(ResultEnum.SUCCESS);
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
		return new Result(ResultEnum.SUCCESS, new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getResult()));
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)<br>
	 * 在可能会对数据进行二次处理,导致类型变化时使用.<br>
	 * result.data的类型为frodez.util.beans.result.PageData
	 * @see frodez.util.beans.result.PageData
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(Page<?> page, Collection<T> data) {
		Assert.notNull(page, "page must not be null");
		Assert.notNull(data, "data must not be null");
		return new Result(ResultEnum.SUCCESS, new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(), data));
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
		return new Result(ResultEnum.SUCCESS, new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getList()));
	}

	/**
	 * 返回分页查询类型结果(仅在成功时使用)<br>
	 * 在可能会对数据进行二次处理,导致类型变化时使用.<br>
	 * result.data的类型为frodez.util.beans.result.PageData
	 * @see frodez.util.beans.result.PageData
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static <T> Result page(PageInfo<?> page, Collection<T> data) {
		Assert.notNull(page, "page must not be null");
		Assert.notNull(data, "data must not be null");
		return new Result(ResultEnum.SUCCESS, new PageData<>(page.getPageNum(), page.getPageSize(), page.getTotal(), data));
	}

	/**
	 * 返回失败结果(无信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result fail() {
		return ResultHelper.DEFAULT_CACHE.get(ResultEnum.FAIL);
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
		return ResultHelper.DEFAULT_CACHE.get(ResultEnum.ERROR_PARAMETER);
	}

	/**
	 * 返回请求参数错误(有信息)
	 * @author Frodez
	 * @date 2019-02-02
	 */
	public static Result errorRequest(String message) {
		Assert.notNull(message, "message must not be null");
		return new Result(message, ResultEnum.ERROR_PARAMETER);
	}

	/**
	 * 返回服务器错误结果(无信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result errorService() {
		return ResultHelper.DEFAULT_CACHE.get(ResultEnum.ERROR_SERVICE);
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
	 * 返回服务器错误结果(有信息)
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static Result errorService(Throwable message) {
		Assert.notNull(message, "message must not be null");
		return new Result(message.getMessage(), ResultEnum.ERROR_SERVICE);
	}

	/**
	 * 返回未登录结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result notLogin() {
		return ResultHelper.DEFAULT_CACHE.get(ResultEnum.NOT_LOGIN);
	}

	/**
	 * 返回已过期结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result expired() {
		return ResultHelper.DEFAULT_CACHE.get(ResultEnum.EXPIRED);
	}

	/**
	 * 返回未通过验证结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result noAuth() {
		return ResultHelper.DEFAULT_CACHE.get(ResultEnum.NO_AUTH);
	}

	/**
	 * 返回缺少操作权限结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result noAccess() {
		return ResultHelper.DEFAULT_CACHE.get(ResultEnum.NO_ACCESS);
	}

	/**
	 * 返回重复请求结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result repeatRequest() {
		return ResultHelper.DEFAULT_CACHE.get(ResultEnum.REPEAT_REQUEST);
	}

	/**
	 * 返回服务器繁忙结果
	 * @author Frodez
	 * @date 2019-03-02
	 */
	public static Result busy() {
		return ResultHelper.DEFAULT_CACHE.get(ResultEnum.BUSY);
	}

	/**
	 * 根据类型获取数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> T as(Class<T> klass) throws ClassCastException, ResultParseException {
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
	public <T> PageData<T> pageData(Class<T> klass) throws ClassCastException, ResultParseException {
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
	public <T> List<T> list(Class<T> klass) throws ClassCastException, ResultParseException {
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
	public <T> Set<T> set(Class<T> klass) throws ClassCastException, ResultParseException {
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
	public Map<String, Object> map() throws ClassCastException, ResultParseException {
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
	public <K, V> Map<K, V> map(Class<K> keyClass, Class<V> valueClass) throws ClassCastException, ResultParseException {
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
	 * 判断是否可用,如果不可用,则抛出Exception
	 * @author Frodez
	 * @param <E>
	 * @throws Exception
	 * @date 2019-12-07
	 */
	public <E extends Throwable> Result orThrow(Function<Result, ? extends E> function) throws E {
		if (code != ResultEnum.SUCCESS.val) {
			throw function.apply(this);
		}
		return this;
	}

	/**
	 * 判断是否可用,如果不可用,则抛出ServiceException
	 * @see frodez.constant.errors.code.ServiceException.ServiceException
	 * @author Frodez
	 * @date 2019-12-07
	 */
	public Result orThrowMessage() {
		if (code != ResultEnum.SUCCESS.val) {
			throw new ServiceException(this.message);
		}
		return this;
	}

	/**
	 * 判断是否可用,如果不可用,则抛出Exception
	 * @author Frodez
	 * @param <E>
	 * @throws Exception
	 * @date 2019-12-07
	 */
	public <E extends Throwable> Result orThrowMessage(Function<String, ? extends E> function) throws E {
		if (code != ResultEnum.SUCCESS.val) {
			throw function.apply(this.message);
		}
		return this;
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

	/**
	 * 获取json字符串
	 * @author Frodez
	 * @date 2019-05-24
	 */
	public String json() {
		return new String(jsonBytes());
	}

	/**
	 * 获取json字符串(数组形式)<br>
	 * <strong>禁止对jsonBytes做任何修改!!!</strong>
	 * @author Frodez
	 * @date 2019-05-24
	 */
	@SneakyThrows
	public byte[] jsonBytes() {
		return cacheBytes != null ? cacheBytes : ResultHelper.writer.writeValueAsBytes(this);
	}

	/**
	 * 获取result的json字符串缓存(数组形式)<br>
	 * 仅当为默认Result时使用.其他时候为空.<br>
	 * <strong>禁止对cacheBytes做任何修改!!!</strong>
	 * @author Frodez
	 * @date 2018-11-27
	 */
	public byte[] cacheBytes() {
		return cacheBytes;
	}

	/**
	 * 获取ObjectWriter
	 * @author Frodez
	 * @date 2019-05-24
	 */
	public static ObjectWriter writer() {
		return ResultHelper.writer;
	}

	/**
	 * 获取ObjectReader
	 * @author Frodez
	 * @date 2019-05-24
	 */
	public static ObjectReader reader() {
		return ResultHelper.reader;
	}

	private void ableAndNotNull() {
		if (code != ResultEnum.SUCCESS.val) {
			throw new UnsupportedOperationException(message);
		}
		if (data == null) {
			throw new ResultParseException("数据为空!");
		}
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
	 * 返回状态枚举
	 * @author Frodez
	 * @date 2018-11-13
	 */
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
		ERROR_PARAMETER(1002, HttpStatus.BAD_REQUEST, "请求参数错误"),
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
		@Getter
		private int val;

		/**
		 * http状态码
		 */
		@Getter
		private HttpStatus status;

		/**
		 * 描述
		 */
		@Getter
		private String desc;

		private static final Map<Integer, ResultEnum> enumMap;

		static {
			var builder = ImmutableMap.<Integer, ResultEnum>builder();
			for (ResultEnum iter : ResultEnum.values()) {
				builder.put(iter.val, iter);
			}
			enumMap = builder.build();
		}

		public static ResultEnum of(Integer value) {
			return enumMap.get(value);
		}

	}

	/**
	 * Result解析异常
	 * @author Frodez
	 * @date 2019-01-21
	 */
	public class ResultParseException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public ResultParseException(String message) {
			super(message, null, false, false);
		}

		public ResultParseException(String... message) {
			super(StrUtil.concat(message), null, false, false);
		}

		public ResultParseException() {
			super(null, null, false, false);
		}

	}

	/**
	 * Result工具类
	 * @author Frodez
	 * @date 2019-12-03
	 */
	@UtilityClass
	private static class ResultHelper {

		/**
		 * 默认类型实例
		 */
		static transient final EnumMap<ResultEnum, Result> DEFAULT_CACHE = new EnumMap<>(ResultEnum.class);

		/**
		 * jackson writer
		 */
		static transient ObjectWriter writer;

		/**
		 * jackson writer
		 */
		static transient ObjectReader reader;

		static {
			writer = JSONUtil.mapper().writerFor(Result.class);
			reader = JSONUtil.mapper().readerFor(Result.class);
			for (ResultEnum item : ResultEnum.values()) {
				Result result = new Result(item);
				try {
					result.cacheBytes = writer.writeValueAsBytes(result);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
				DEFAULT_CACHE.put(item, result);
			}
			Assert.notNull(writer, "writer must not be null");
			Assert.notNull(reader, "reader must not be null");
			Assert.notNull(DEFAULT_CACHE, "DEFAULT_CACHE must not be null");
		}

	}

}
