package frodez.constant.keys.config;

/**
 * 全局配置转换器
 * @author Frodez
 * @date 2020-01-01
 */
public interface GlobalDataKey<T> {

	/**
	 * 转换数据
	 * @author Frodez
	 * @date 2020-01-01
	 */
	T revert(String value);

	/**
	 * 检查设置合法性
	 * @author Frodez
	 * @param <V>
	 * @date 2020-01-01
	 */
	static Enum<? extends GlobalDataKey<?>> check(Byte type, String name) {
		switch (type) {
			case 1 : {
				return BoolKey.valueOf(name);
			}
			case 2 : {
				return IntKey.valueOf(name);
			}
			case 3 : {
				return DoubleKey.valueOf(name);
			}
			case 4 : {
				return StringKey.valueOf(name);
			}
			default : {
				throw new IllegalArgumentException(type.toString());
			}
		}
	}

}
