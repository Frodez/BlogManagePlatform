package frodez.service.user.facade;

import frodez.dao.param.user.AddPermission;
import frodez.dao.param.user.AddRole;
import frodez.dao.param.user.QueryRolePermission;
import frodez.dao.param.user.UpdatePermission;
import frodez.dao.param.user.UpdateRole;
import frodez.dao.param.user.UpdateRolePermission;
import frodez.util.annotations.decoration.ServiceOnly;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import java.util.List;

/**
 * 权限信息服务
 * @author Frodez
 * @date 2018-11-14
 */
public interface IAuthorityService {

	/**
	 * 根据用户ID查询用户基本信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getUserInfo(Long userId);

	/**
	 * 根据用户名获取用户基本信息
	 * @author Frodez
	 * @date 2018-11-14
	 */
	Result getUserInfo(String userName);

	/**
	 * 分页查询用户基本信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	Result getUserInfos(QueryPage param);

	/**
	 * 根据用户ID批量获取用户基本信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	Result getUserInfosByIds(List<Long> userIds, boolean includeFobiddens);

	/**
	 * 根据用户名批量获取用户基本信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	Result getUserInfosByNames(List<String> userNames, boolean includeFobiddens);

	/**
	 * 根据用户ID更新用户基本信息
	 * @author Frodez
	 * @date 2019-03-16
	 */
	@ServiceOnly
	Result refreshUserInfoByIds(List<Long> userIds, boolean includeFobiddens);

	/**
	 * 根据用户名更新用户基本信息
	 * @author Frodez
	 * @date 2019-03-16
	 */
	@ServiceOnly
	Result refreshUserInfoByNames(List<String> userNames, boolean includeFobiddens);

	/**
	 * 获取权限信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	Result getPermission(Long permissionId);

	/**
	 * 分页查询权限信息
	 * @author Frodez
	 * @date 2018-03-06
	 */
	Result getPermissions(QueryPage param);

	/**
	 * 获取角色信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	Result getRole(Long roleId);

	/**
	 * 分页查询角色信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getRoles(QueryPage param);

	/**
	 * 根据角色ID获取对应权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getRolePermissions(QueryRolePermission param);

	/**
	 * 添加新角色
	 * @author Frodez
	 * @date 2019-03-15
	 */
	Result addRole(AddRole param);

	/**
	 * 修改角色
	 * @author Frodez
	 * @date 2019-03-17
	 */
	Result updateRole(UpdateRole param);

	/**
	 * 添加新权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	Result addPermission(AddPermission param);

	/**
	 * 修改权限
	 * @author Frodez
	 * @date 2019-03-17
	 */
	Result updatePermission(UpdatePermission param);

	/**
	 * 修改角色权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	Result updateRolePermission(UpdateRolePermission param);

	/**
	 * 删除角色
	 * @author Frodez
	 * @date 2019-03-18
	 */
	Result removeRole(Long roleId);

	/**
	 * 删除权限
	 * @author Frodez
	 * @date 2019-03-18
	 */
	Result removePermission(Long permissionId);

	/**
	 * 扫描系统中所有端点并添加权限
	 * @author Frodez
	 * @date 2019-03-18
	 */
	Result scanAndCreatePermissions();

}
