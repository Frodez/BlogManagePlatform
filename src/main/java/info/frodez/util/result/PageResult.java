package info.frodez.util.result;

import java.util.List;
import java.util.Map;
import java.util.Set;

import info.frodez.util.json.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 分页查询返回参数
 * @author Frodez
 * @date 2018-11-13
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageResult extends Result {

	/**
	 * 总数
	 */
	private Integer total = 0;

	public PageResult(String message, ResultEnum status) {
		super(message, status);
	}

	public PageResult(String message, ResultEnum status, Object data, Integer total) {
		super(message, status, data);
		this.total = total;
	}

	public PageResult(ResultEnum status, Object data, Integer total) {
		super(status, data);
		this.total = total;
	}

	public PageResult(ResultEnum status) {
		super(status);
	}

	/**
	 * 根据类型获取数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@Override
	public <T> T parse(Class<T> klass) {
		return super.parse(klass);
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@Override
	public <T> List<T> parseList(Class<T> klass) {
		return super.parseList(klass);
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@Override
	public <T> Set<T> parseSet(Class<T> klass) {
		return super.parseSet(klass);
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@Override
	public Map<String, Object> parseMap() {
		return super.parseMap();
	}

	/**
	 * 根据类型获取集合数据,data为空时抛出异常
	 * @author Frodez
	 * @param klass 类型
	 * @date 2018-11-13
	 */
	@Override
	public <K, V> Map<K, V> parseMap(Class<K> keyClass, Class<V> valueClass) {
		return super.parseMap(keyClass, valueClass);
	}

	/**
	 * 判断是否成功,boolean true:成功 false:非成功
	 * @author Frodez
	 * @date 2018-11-13
	 */
	@Override
	public boolean success() {
		return super.success();
	}

	/**
	 * 判断是否成功,true:非成功 false:成功
	 * @author Frodez
	 * @date 2018-11-13
	 */
	@Override
	public boolean notSuccess() {
		return super.notSuccess();
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
