package frodez.service.cache.facade;

import frodez.dao.result.user.UserInfo;

public interface TokenCache {

	/**
	 * 获取缓存大小
	 * @author Frodez
	 * @date 2019-12-25
	 */
	long size();

	/**
	 * 判断token是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	boolean existToken(String token);

	/**
	 * 判断id是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	boolean existId(Long id);

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
	 * 通过id获取token
	 * @author Frodez
	 * @date 2019-03-16
	 */
	String getTokenById(Long id);

	/**
	 * 根据token删除对应缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	void remove(String token);

}
