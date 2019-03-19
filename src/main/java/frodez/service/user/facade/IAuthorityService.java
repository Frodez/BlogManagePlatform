package frodez.service.user.facade;

import frodez.dao.param.user.AddPermission;
import frodez.dao.param.user.AddRole;
import frodez.dao.param.user.QueryRolePermission;
import frodez.dao.param.user.UpdatePermission;
import frodez.dao.param.user.UpdateRole;
import frodez.dao.param.user.UpdateRolePermission;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.constant.annotation.ServiceOnly;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
	Result getUserInfo(@NotNull Long userId);

	/**
	 * 根据用户名获取用户基本信息
	 * @author Frodez
	 * @date 2018-11-14
	 */
	Result getUserInfo(@NotBlank String userName);

	/**
	 * 分页查询用户基本信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	Result getUserInfos(@Valid @NotNull QueryPage param);

	/**
	 * 根据用户ID批量获取用户基本信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	Result getUserInfosByIds(@NotEmpty List<Long> userIds, boolean includeFobiddens);

	/**
	 * 根据用户名批量获取用户基本信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	Result getUserInfosByNames(@NotEmpty List<String> userNames, boolean includeFobiddens);

	/**
	 * 根据用户ID更新用户基本信息
	 * @author Frodez
	 * @date 2019-03-16
	 */
	@ServiceOnly
	Result refreshUserInfoByIds(@NotEmpty List<Long> userIds, boolean includeFobiddens);

	/**
	 * 根据用户名更新用户基本信息
	 * @author Frodez
	 * @date 2019-03-16
	 */
	@ServiceOnly
	Result refreshUserInfoByNames(@NotEmpty List<String> userNames, boolean includeFobiddens);

	/**
	 * 分页查询权限信息
	 * @author Frodez
	 * @date 2018-03-06
	 */
	Result getPermissions(@Valid @NotNull QueryPage param);

	/**
	 * 分页查询角色信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getRoles(@Valid @NotNull QueryPage param);

	/**
	 * 根据角色ID获取对应权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getRolePermissions(@Valid @NotNull QueryRolePermission param);

	/**
	 * 添加新角色
	 * @author Frodez
	 * @date 2019-03-15
	 */
	Result addRole(@Valid @NotNull AddRole param);

	/**
	 * 修改角色
	 * @author Frodez
	 * @date 2019-03-17
	 */
	Result updateRole(@Valid @NotNull UpdateRole param);

	/**
	 * 添加新权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	Result addPermission(@Valid @NotNull AddPermission param);

	/**
	 * 修改权限
	 * @author Frodez
	 * @date 2019-03-17
	 */
	Result updatePermission(@Valid @NotNull UpdatePermission param);

	/**
	 * 修改角色权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	Result updateRolePermission(@Valid @NotNull UpdateRolePermission param);

	/**
	 * 删除角色
	 * @author Frodez
	 * @date 2019-03-18
	 */
	Result removeRole(@NotNull Long roleId);

	/**
	 * 删除权限
	 * @author Frodez
	 * @date 2019-03-18
	 */
	Result removePermission(@NotNull Long permissionId);

	/**
	 * 扫描系统中所有端点并添加权限
	 * @author Frodez
	 * @date 2019-03-18
	 */
	Result scanAndCreatePermissions();

}
