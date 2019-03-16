package frodez.service.cache.vm.facade;

import frodez.dao.result.user.UserInfo;
import frodez.service.cache.base.ICache;
import java.util.List;
import java.util.function.Predicate;

public interface TokenCache extends ICache<String, UserInfo> {

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
	 * 通过自定义查询条件获取token,如果存在多个,只返回其中一个
	 * @author Frodez
	 * @date 2019-03-16
	 */
	String getTokenByCondition(Predicate<UserInfo> predicate);

	/**
	 * 通过自定义查询条件获取tokens
	 * @author Frodez
	 * @date 2019-03-16
	 */
	List<String> getTokensByCondition(Predicate<UserInfo> predicate);

	/**
	 * 根据token删除对应缓存
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Override
	void remove(String token);

}
