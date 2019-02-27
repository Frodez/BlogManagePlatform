package frodez.config.security.login.cache.facade;

import frodez.service.user.result.UserInfo;

public interface TokenCache {

	/**
	 * 判断token是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	boolean exist(String token);

	/**
	 * 判断userInfo是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	boolean exist(UserInfo userInfo);

	/**
	 * 存储token和userInfo于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	void save(String token, UserInfo userInfo);

	/**
	 * 通过token获取缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	UserInfo get(String token);

	/**
	 * 根据token删除对应缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	void remove(String token);

}
