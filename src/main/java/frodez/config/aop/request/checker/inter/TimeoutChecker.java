package frodez.config.aop.request.checker.inter;

/**
 * 自动超时型重复请求检查接口
 * @author Frodez
 * @date 2019-01-21
 */
public interface TimeoutChecker extends Checker {

	/**
	 * 加锁
	 * @param key 对应key
	 * @param timeout 超时时间
	 * @author Frodez
	 * @date 2019-01-21
	 */
	void lock(String key, long timeout);

}
