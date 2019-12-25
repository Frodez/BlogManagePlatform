package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.constant.annotations.decoration.ServiceOnly;
import frodez.constant.settings.DefPage;
import frodez.dao.model.user.PagePermission;
import frodez.dao.model.user.Permission;
import frodez.dao.model.user.Role;
import frodez.dao.param.user.AddPagePermission;
import frodez.dao.param.user.AddPermission;
import frodez.dao.param.user.AddRole;
import frodez.dao.param.user.QueryRolePermission;
import frodez.dao.param.user.UpdatePagePermission;
import frodez.dao.param.user.UpdatePermission;
import frodez.dao.param.user.UpdateRole;
import frodez.dao.param.user.UpdateRolePermission;
import frodez.dao.result.user.PagePermissionDetail;
import frodez.dao.result.user.PagePermissionInfo;
import frodez.dao.result.user.PermissionDetail;
import frodez.dao.result.user.PermissionInfo;
import frodez.dao.result.user.RoleDetail;
import frodez.dao.result.user.UserInfo;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	@Success(UserInfo.class)
	Result getUserInfo(@NotNull Long userId);

	/**
	 * 根据用户名获取用户基本信息
	 * @author Frodez
	 * @date 2018-11-14
	 */
	@Check
	@Success(UserInfo.class)
	Result getUserInfo(@NotBlank String userName);

	/**
	 * 分页查询用户基本信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	@Check
	@Success(value = UserInfo.class, containerType = Container.PAGE)
	Result getUserInfos(@Valid @NotNull QueryPage param);

	/**
	 * 根据用户ID批量获取用户基本信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	@Check
	@Success(value = UserInfo.class, containerType = Container.LIST)
	Result getUserInfosByIds(@NotEmpty @Size(max = DefPage.MAX_SIZE) List<Long> userIds, boolean includeFobiddens);

	/**
	 * 根据用户名批量获取用户基本信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	@Check
	@Success(value = UserInfo.class, containerType = Container.LIST)
	Result getUserInfosByNames(@NotEmpty @Size(max = DefPage.MAX_SIZE) List<String> userNames, boolean includeFobiddens);

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
	@Success(PermissionDetail.class)
	Result getPermission(@NotNull Long permissionId);

	/**
	 * 分页查询权限信息
	 * @author Frodez
	 * @date 2018-03-06
	 */
	@Check
	@Success(value = Permission.class, containerType = Container.PAGE)
	Result getPermissions(@Valid @NotNull QueryPage param);

	/**
	 * 获取页面资源权限信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	@Check
	@Success(PagePermissionDetail.class)
	Result getPagePermission(@NotNull Long pagePermissionId);

	/**
	 * 获取页面资源权限信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	@Check
	@Success(value = PagePermission.class, containerType = Container.PAGE)
	Result getPagePermissions(@Valid @NotNull QueryPage param);

	/**
	 * 获取角色信息
	 * @author Frodez
	 * @date 2019-03-19
	 */
	@Check
	@Success(RoleDetail.class)
	Result getRole(@NotNull Long roleId);

	/**
	 * 分页查询角色信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@Check
	@Success(value = Role.class, containerType = Container.PAGE)
	Result getRoles(@Valid @NotNull QueryPage param);

	/**
	 * 根据角色ID获取对应权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@Check
	@Success(value = PagePermissionInfo.class, containerType = Container.PAGE)
	Result getRolePagePermissions(@Valid @NotNull QueryRolePermission param);

	/**
	 * 根据角色ID获取对应权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@Check
	@Success(value = PermissionInfo.class, containerType = Container.PAGE)
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
	 * 添加新页面资源权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	@Check
	Result addPagePermission(@Valid @NotNull AddPagePermission param);

	/**
	 * 修改页面资源权限
	 * @author Frodez
	 * @date 2019-03-17
	 */
	@Check
	Result updatePagePermission(@Valid @NotNull UpdatePagePermission param);

	/**
	 * 修改角色权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	@Check
	Result updateRolePermission(@Valid @NotNull UpdateRolePermission param);

	/**
	 * 修改角色页面资源权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	@Check
	Result updateRolePagePermission(@Valid @NotNull UpdateRolePermission param);

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
	 * 删除页面资源权限
	 * @author Frodez
	 * @date 2019-03-18
	 */
	@Check
	Result removePagePermission(@NotNull Long pagePermissionId);

	/**
	 * 扫描系统中所有端点并添加权限
	 * @author Frodez
	 * @date 2019-03-18
	 */
	Result scanAndCreatePermissions();

}
