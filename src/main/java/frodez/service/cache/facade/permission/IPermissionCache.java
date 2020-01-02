package frodez.service.cache.facade.permission;

import frodez.config.cache.ICache;
import frodez.dao.model.result.permission.PermissionDetail;

/**
 * 角色权限缓存
 * @author Frodez
 * @date 2020-01-01
 */
public interface IPermissionCache extends ICache {

	/**
	 * 判断角色id对应权限是否存在
	 * @author Frodez
	 * @date 2019-12-29
	 */
	boolean exist(Long id);

	/**
	 * 获取角色id对应的权限
	 * @author Frodez
	 * @date 2020-01-01
	 */
	PermissionDetail get(Long roleId);

	/**
	 * 保存角色id对应的权限
	 * @author Frodez
	 * @date 2020-01-01
	 */
	void save(Long roleId, PermissionDetail detail);

}
