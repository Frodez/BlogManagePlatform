package frodez.config.aop.request.checker.facade;

/**
 * 阻塞型重复请求检查接口
 * @author Frodez
 * @date 2019-01-21
 */
public interface ManualChecker extends Checker {

	/**
	 * 加锁
	 * @param key 对应key
	 * @author Frodez
	 * @date 2019-01-21
	 */
	void lock(String key);

	/**
	 * 解锁
	 * @param key 对应key
	 * @author Frodez
	 * @date 2019-01-21
	 */
	void free(String key);

}
