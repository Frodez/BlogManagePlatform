package frodez.constant.enums;

/**
 * 业务逻辑通用枚举接口
 * @author Frodez
 * @date 2019-06-19
 */
public interface IEnum<V> {

	/**
	 * 获取枚举对应的值
	 * @author Frodez
	 * @date 2019-06-19
	 */
	V getVal();

	/**
	 * 获取枚举对应的描述
	 * @author Frodez
	 * @date 2019-06-19
	 */
	String getDesc();

}
