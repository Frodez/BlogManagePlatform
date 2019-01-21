package info.frodez.config.aop.request;

/**
 * 重复请求检查接口
 * @author Frodez
 * @date 2019-01-21
 */
public interface RepeatChecker {

	/**
	 * 检查key是否存在,存在返回true,否则返回false
	 * @param key 对应key
	 * @param params 额外参数(可选)
	 * @author Frodez
	 * @date 2019-01-21
	 */
	boolean check(String key, Object... params);

	/**
	 * 加锁
	 * @param key 对应key
	 * @param params 额外参数(可选)
	 * @author Frodez
	 * @date 2019-01-21
	 */
	void lock(String key, Object... params);

	/**
	 * 解锁
	 * @param key 对应key
	 * @param params 额外参数(可选)
	 * @author Frodez
	 * @date 2019-01-21
	 */
	void free(String key, Object... params);

}
