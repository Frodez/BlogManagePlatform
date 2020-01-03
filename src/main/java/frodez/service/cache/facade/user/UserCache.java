package frodez.service.cache.facade.user;

import frodez.config.cache.ICache;
import frodez.dao.model.result.user.UserBaseInfo;

/**
 * id-User的缓存<br>
 * 用途：缓存用户信息
 * @author Frodez
 * @date 2019-12-29
 */
public interface UserCache extends ICache {

	/**
	 * 根据id获取UserBaseInfo
	 * @author Frodez
	 * @date 2019-12-29
	 */
	UserBaseInfo get(Long id);

	/**
	 * 存储id-UserBaseInfo
	 * @author Frodez
	 * @date 2019-12-29
	 */
	void save(Long id, UserBaseInfo user);

	/**
	 * 根据id删除User
	 * @author Frodez
	 * @date 2019-12-29
	 */
	void remove(Long id);

}
