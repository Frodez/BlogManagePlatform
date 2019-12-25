package frodez.service.cache.facade;

import frodez.dao.result.user.UserInfo;

public interface NameCache {

	/**
	 * 获取缓存大小
	 * @author Frodez
	 * @date 2019-12-25
	 */
	long size();

	/**
	 * 判断name是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	boolean existKey(String name);

	/**
	 * 判断userInfo是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	boolean existValue(UserInfo userInfo);

	/**
	 * 存储name和userInfo于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	void save(String name, UserInfo userInfo);

	/**
	 * 通过name获取缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	UserInfo get(String name);

	/**
	 * 根据name删除对应缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	void remove(String name);

}
