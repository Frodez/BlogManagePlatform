package frodez.constant.keys.config;

/**
 * 布尔类型的全局配置
 * @author Frodez
 * @date 2020-01-01
 */
public enum BoolKey implements GlobalDataKey<Boolean> {

	A;

	@Override
	public Boolean revert(String value) {
		return Boolean.valueOf(value);
	}

}
