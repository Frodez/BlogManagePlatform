package frodez.service.cache.facade.user;

import frodez.config.cache.ICache;
import frodez.dao.model.table.user.Role;
import java.util.List;

/**
 * id-Role的缓存<br>
 * 用途：缓存角色信息
 * @author Frodez
 * @date 2019-12-29
 */
public interface RoleCache extends ICache {

	/**
	 * 根据id获取Role
	 * @author Frodez
	 * @date 2019-12-29
	 */
	Role get(Long id);

	/**
	 * 存储id-Role
	 * @author Frodez
	 * @date 2019-12-29
	 */
	void save(Long id, Role role);

	/**
	 * 存储id-Role
	 * @author Frodez
	 * @date 2019-12-29
	 */
	void save(List<Long> ids, Role role);

	/**
	 * 根据id删除User
	 * @author Frodez
	 * @date 2019-12-29
	 */
	void remove(Long id);

}
