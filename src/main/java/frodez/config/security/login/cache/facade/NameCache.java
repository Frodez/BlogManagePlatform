package frodez.config.security.login.cache.facade;

import frodez.service.user.result.UserInfo;

public interface NameCache {

	/**
	 * 判断name是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	boolean exist(String name);

	/**
	 * 判断userInfo是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	boolean exist(UserInfo userInfo);

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
