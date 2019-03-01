package frodez.service.cache.vm.facade;

import frodez.dao.result.user.UserInfo;
import frodez.service.cache.base.ICache;

public interface NameCache extends ICache<String, UserInfo> {

	/**
	 * 判断name是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	boolean existKey(String name);

	/**
	 * 判断userInfo是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	boolean existValue(UserInfo userInfo);

	/**
	 * 存储name和userInfo于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	void save(String name, UserInfo userInfo);

	/**
	 * 通过name获取缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	UserInfo get(String name);

	/**
	 * 根据name删除对应缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	void remove(String name);

}
