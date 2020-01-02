package frodez.constant.keys.config;

/**
 * 字符串类型的全局配置
 * @author Frodez
 * @date 2020-01-01
 */
public enum StringKey implements GlobalDataKey<String> {

	A;

	@Override
	public String revert(String value) {
		return value;
	}

}
