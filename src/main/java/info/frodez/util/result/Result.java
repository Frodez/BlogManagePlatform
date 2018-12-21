package info.frodez.util.result;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

import info.frodez.util.json.JSONUtil;
import lombok.Data;

/**
 * 通用返回参数
 * @author Frodez
 * @date 2018-11-13
 */
@Data
public class Result {

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

	public Result(ResultEnum status, String message) {
		this.status = status.getValue();
		this.message = message;
	}

	public Result(ResultEnum status, String message, Object data) {
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
	 * @date 2018-11-13
	 */
	@JsonIgnore
	public <T> T getData(Class<T> klass) {
		if(data == null) {
			throw new NullPointerException("数据为空!");
		}
		return klass.cast(data);
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public <T> List<T> getListData(Class<T> klass) {
		if(data == null) {
			throw new NullPointerException("数据为空!");
		}
		return (List<T>) data;
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public <T> Set<T> getSetData(Class<T> klass) {
		if(data == null) {
			throw new NullPointerException("数据为空!");
		}
		return (Set<T>) data;
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public Map<String, Object> getMapData() {
		if(data == null) {
			throw new NullPointerException("数据为空!");
		}
		return (Map<String, Object>) data;
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public <K, V> Map<K, V> getMapData(Class<K> keyClass, Class<V> valueClass) {
		if(data == null) {
			throw new NullPointerException("数据为空!");
		}
		return (Map<K, V>) data;
	}

	/**
	 * 判断是否成功,boolean true:成功 false:非成功
	 * @author Frodez
	 * @date 2018-11-13
	 */
	@JsonIgnore
	public boolean isSuccess() {
		return status == ResultUtil.SUCCESS_VALUE;
	}

	/**
	 * 判断是否成功,true:非成功 false:成功
	 * @author Frodez
	 * @date 2018-11-13
	 */
	@JsonIgnore
	public boolean isNotSuccess() {
		return status != ResultUtil.SUCCESS_VALUE;
	}

	/**
	 * 获取result的json字符串,如果存在异常,返回null
	 * @author Frodez
	 * @date 2018-11-27
	 */
	@Override
	public String toString() {
		try {
			return JSONUtil.getInstance().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

}
