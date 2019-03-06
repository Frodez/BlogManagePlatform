package frodez.service.cache.vm.facade;

import frodez.dao.result.user.UserInfo;
import frodez.service.cache.base.ICache;

public interface UserIdCache extends ICache<Long, UserInfo> {

	/**
	 * 判断userId是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	boolean existKey(Long userId);

	/**
	 * 判断userInfo是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	boolean existValue(UserInfo userInfo);

	/**
	 * 存储userId和userInfo于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	void save(Long userId, UserInfo userInfo);

	/**
	 * 通过userId获取缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	UserInfo get(Long userId);

	/**
	 * 根据userId删除对应缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	void remove(Long userId);

}
