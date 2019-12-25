package frodez.dao.mapper.user;

import frodez.config.mybatis.DataMapper;
import frodez.dao.model.user.RolePermission;
import frodez.dao.result.user.PagePermissionInfo;
import frodez.dao.result.user.PermissionInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description 角色权限表
 * @table tb_role_permission
 * @date 2019-03-15
 */
@Repository
public interface RolePermissionMapper extends DataMapper<RolePermission> {

	/**
	 * 根据角色ID获取权限信息
	 * @author Frodez
	 * @param roleId 角色ID
	 * @date 2018-11-14
	 */
	List<PermissionInfo> getPermissions(@Param("roleId") Long roleId);

	/**
	 * 根据角色ID获取权限信息
	 * @author Frodez
	 * @param roleId 角色ID
	 * @date 2018-11-14
	 */
	List<PermissionInfo> batchGetPermissions(@Param("roleIds") List<Long> roleIds);

	/**
	 * 根据角色ID获取页面资源权限信息
	 * @author Frodez
	 * @param roleId 角色ID
	 * @date 2018-11-14
	 */
	List<PagePermissionInfo> getPagePermissions(@Param("roleId") Long roleId);

	/**
	 * 根据角色ID获取页面资源权限信息
	 * @author Frodez
	 * @param roleId 角色ID
	 * @date 2018-11-14
	 */
	List<PagePermissionInfo> batchGetPagePermissions(@Param("roleIds") List<Long> roleIds);

}
