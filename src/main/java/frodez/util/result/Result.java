package frodez.util.result;

import frodez.config.error.exception.ParseException;
import frodez.util.json.JSONUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

/**
 * 通用返回参数
 * @author Frodez
 * @date 2018-11-13
 */
@Data
@NoArgsConstructor
public class Result implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 状态
	 */
	private Integer code;

	/**
	 * 数据
	 */
	private Object data;

	public Result(String message, ResultEnum status) {
		this.message = message;
		this.code = status.getVal();
	}

	public Result(String message, ResultEnum status, Object data) {
		this.message = message;
		this.code = status.getVal();
		this.data = data;
	}

	public Result(ResultEnum status, Object data) {
		this.message = status.getDesc();
		this.code = status.getVal();
		this.data = data;
	}

	public Result(ResultEnum status) {
		this.message = status.getDesc();
		this.code = status.getVal();
	}

	/**
	 * 根据类型获取数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	public <T> T as(Class<T> klass) throws ClassCastException {
		Assert.notNull(klass, "类型不能为空!");
		if (code != ResultEnum.SUCCESS.getVal()) {
			throw new RuntimeException(message);
		}
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return klass.cast(data);
	}

	/**
	 * 根据类型获取分页类型数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> PageVO<T> asPage(Class<T> klass) throws ClassCastException {
		Assert.notNull(klass, "类型不能为空!");
		if (code != ResultEnum.SUCCESS.getVal()) {
			throw new RuntimeException(message);
		}
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (PageVO<T>) data;
	}

	/**
	 * 根据类型获取集合数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> asList(Class<T> klass) throws ClassCastException {
		Assert.notNull(klass, "类型不能为空!");
		if (code != ResultEnum.SUCCESS.getVal()) {
			throw new RuntimeException(message);
		}
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (List<T>) data;
	}

	/**
	 * 根据类型获取集合数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> Set<T> asSet(Class<T> klass) throws ClassCastException {
		Assert.notNull(klass, "类型不能为空!");
		if (code != ResultEnum.SUCCESS.getVal()) {
			throw new RuntimeException(message);
		}
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (Set<T>) data;
	}

	/**
	 * 根据类型获取集合数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> asMap() throws ClassCastException {
		if (code != ResultEnum.SUCCESS.getVal()) {
			throw new RuntimeException(message);
		}
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (Map<String, Object>) data;
	}

	/**
	 * 根据类型获取集合数据,不成功或者data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> asMap(Class<K> keyClass, Class<V> valueClass) throws ClassCastException {
		Assert.notNull(keyClass, "键类型不能为空!");
		Assert.notNull(valueClass, "值类型不能为空!");
		if (code != ResultEnum.SUCCESS.getVal()) {
			throw new RuntimeException(message);
		}
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (Map<K, V>) data;
	}

	/**
	 * 判断是否成功,不成功抛出RuntimeException
	 * @author Frodez
	 * @date 2019-02-13
	 */
	public void check() {
		if (code != ResultEnum.SUCCESS.getVal()) {
			throw new RuntimeException(message);
		}
	}

	/**
	 * 判断是否成功,boolean true:成功 false:非成功
	 * @author Frodez
	 * @date 2018-11-13
	 */
	public boolean success() {
		return code == ResultEnum.SUCCESS.getVal();
	}

	/**
	 * 判断是否成功,true:非成功 false:成功
	 * @author Frodez
	 * @date 2018-11-13
	 */
	public boolean notSuccess() {
		return code != ResultEnum.SUCCESS.getVal();
	}

	/**
	 * 获取result的json字符串,如果存在异常,返回null
	 * @author Frodez
	 * @date 2018-11-27
	 */
	@Override
	public String toString() {
		return JSONUtil.toJSONString(this);
	}

}
