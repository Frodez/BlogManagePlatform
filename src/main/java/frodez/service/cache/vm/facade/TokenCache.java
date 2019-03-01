package frodez.service.cache.vm.facade;

import frodez.dao.result.user.UserInfo;

public interface TokenCache extends ICacheService<String, UserInfo> {

	/**
	 * 判断token是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	boolean existKey(String token);

	/**
	 * 判断userInfo是否存在于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	boolean existValue(UserInfo userInfo);

	/**
	 * 存储token和userInfo于缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	void save(String token, UserInfo userInfo);

	/**
	 * 通过token获取缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	UserInfo get(String token);

	/**
	 * 根据token删除对应缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	void remove(String token);

}
