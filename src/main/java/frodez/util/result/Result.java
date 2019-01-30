package frodez.util.result;

import frodez.config.error.exception.ParseException;
import frodez.util.json.JSONUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	 * 状态
	 */
	private Integer status;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 数据
	 */
	private Object data;

	public Result(String message, ResultEnum status) {
		this.status = status.getValue();
		this.message = message;
	}

	public Result(String message, ResultEnum status, Object data) {
		this.status = status.getValue();
		this.message = message;
		this.data = data;
	}

	public Result(ResultEnum status, Object data) {
		this.status = status.getValue();
		this.data = data;
	}

	public Result(ResultEnum status) {
		this.status = status.getValue();
		this.message = status.getDescription();
	}

	/**
	 * 根据类型获取数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	public <T> T parse(Class<T> klass) {
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return klass.cast(data);
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> parseList(Class<T> klass) {
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (List<T>) data;
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <T> Set<T> parseSet(Class<T> klass) {
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (Set<T>) data;
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> parseMap() {
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (Map<String, Object>) data;
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @throws ParseException
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> parseMap(Class<K> keyClass, Class<V> valueClass) {
		if (data == null) {
			throw new ParseException("数据为空!");
		}
		return (Map<K, V>) data;
	}

	/**
	 * 判断是否成功,boolean true:成功 false:非成功
	 * @author Frodez
	 * @date 2018-11-13
	 */
	public boolean success() {
		return status == ResultEnum.SUCCESS.getValue();
	}

	/**
	 * 判断是否成功,true:非成功 false:成功
	 * @author Frodez
	 * @date 2018-11-13
	 */
	public boolean notSuccess() {
		return status != ResultEnum.SUCCESS.getValue();
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
