package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.constant.annotations.decoration.ServiceOnly;
import frodez.dao.param.user.AddPermission;
import frodez.dao.param.user.AddRole;
import frodez.dao.param.user.QueryRolePermission;
import frodez.dao.param.user.UpdatePermission;
import frodez.dao.param.user.UpdateRole;
import frodez.dao.param.user.UpdateRolePermission;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
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
	@Check
	Result getUserInfo(@NotNull Long userId);

	/**
	 * 根据用户名获取用户基本信息
	 * @author Frodez
	 * @date 2018-11-14
	 */
	@Check
	Result getUserInfo(@NotBlank String userName);

	/**
	 * 分页查询用户基本信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	@Check
	Result getUserInfos(@Valid @NotNull QueryPage param);

	/**
	 * 根据用户ID批量获取用户基本信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	@Check
	Result getUserInfosByIds(@NotEmpty List<Long> userIds, boolean includeFobiddens);

	/**
	 * 根据用户名批量获取用户基本信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	@Check
	Result getUserInfosByNames(@NotEmpty List<String> userNames, boolean includeFobiddens);

	/**
	 * 根据用户ID更新用户基本信息
	 * @author Frodez
	 * @date 2019-03-16
	 */
	@Check
	@ServiceOnly
	Result refreshUserInfoByIds(@NotEmpty List<Long> userIds, boolean includeFobiddens);

	/**
	 * 根据用户名更新用户基本信息
	 * @author Frodez
	 * @date 2019-03-16
	 */
	@Check
	@ServiceOnly
	Result refreshUserInfoByNames(@NotEmpty List<String> userNames, boolean includeFobiddens);

	/**
	 * 获取权限信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	@Check
	Result getPermission(@NotNull Long permissionId);

	/**
	 * 分页查询权限信息
	 * @author Frodez
	 * @date 2018-03-06
	 */
	@Check
	Result getPermissions(@Valid @NotNull QueryPage param);

	/**
	 * 获取角色信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	@Check
	Result getRole(@NotNull Long roleId);

	/**
	 * 分页查询角色信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@Check
	Result getRoles(@Valid @NotNull QueryPage param);

	/**
	 * 根据角色ID获取对应权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@Check
	Result getRolePermissions(@Valid @NotNull QueryRolePermission param);

	/**
	 * 添加新角色
	 * @author Frodez
	 * @date 2019-03-15
	 */
	@Check
	Result addRole(@Valid @NotNull AddRole param);

	/**
	 * 修改角色
	 * @author Frodez
	 * @date 2019-03-17
	 */
	@Check
	Result updateRole(@Valid @NotNull UpdateRole param);

	/**
	 * 添加新权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	@Check
	Result addPermission(@Valid @NotNull AddPermission param);

	/**
	 * 修改权限
	 * @author Frodez
	 * @date 2019-03-17
	 */
	@Check
	Result updatePermission(@Valid @NotNull UpdatePermission param);

	/**
	 * 修改角色权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	@Check
	Result updateRolePermission(@Valid @NotNull UpdateRolePermission param);

	/**
	 * 删除角色
	 * @author Frodez
	 * @date 2019-03-18
	 */
	@Check
	Result removeRole(@NotNull Long roleId);

	/**
	 * 删除权限
	 * @author Frodez
	 * @date 2019-03-18
	 */
	@Check
	Result removePermission(@NotNull Long permissionId);

	/**
	 * 扫描系统中所有端点并添加权限
	 * @author Frodez
	 * @date 2019-03-18
	 */
	Result scanAndCreatePermissions();

}
