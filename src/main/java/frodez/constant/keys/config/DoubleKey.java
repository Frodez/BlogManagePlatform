package frodez.constant.keys.config;

/**
 * 浮点数类型的全局配置
 * @author Frodez
 * @date 2020-01-01
 */
public enum DoubleKey implements GlobalDataKey<Double> {

	A;

	@Override
	public Double revert(String value) {
		return Double.valueOf(value);
	}

}
