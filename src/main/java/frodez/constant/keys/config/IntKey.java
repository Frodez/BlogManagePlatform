package frodez.constant.keys.config;

/**
 * 整数类型的全局配置
 * @author Frodez
 * @date 2020-01-01
 */
public enum IntKey implements GlobalDataKey<Long> {

	/**
	 * 默认用户角色ID
	 */
	DEFAULT_USER_ROLE;

	@Override
	public Long revert(String value) {
		return Long.valueOf(value);
	}

}
