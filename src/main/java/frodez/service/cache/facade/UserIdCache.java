package frodez.service.cache.facade;

import frodez.dao.result.user.UserInfo;

public interface UserIdCache {

	/**
	 * 获取缓存大小
	 * @author Frodez
	 * @date 2019-12-25
	 */
	long size();

	/**
	 * 判断userId是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	boolean existKey(Long userId);

	/**
	 * 判断userInfo是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	boolean existValue(UserInfo userInfo);

	/**
	 * 存储userId和userInfo于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	void save(Long userId, UserInfo userInfo);

	/**
	 * 通过userId获取缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	UserInfo get(Long userId);

	/**
	 * 根据userId删除对应缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	void remove(Long userId);

}
