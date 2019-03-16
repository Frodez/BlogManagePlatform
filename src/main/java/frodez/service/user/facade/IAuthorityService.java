package frodez.service.user.facade;

import frodez.dao.param.user.AddPermission;
import frodez.dao.param.user.AddRole;
import frodez.dao.param.user.QueryRolePermission;
import frodez.dao.param.user.SetRolePermission;
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
	 * 根据用户ID查询用户信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getUserInfo(@Valid @NotNull Long userId);

	/**
	 * 根据用户名获取用户信息
	 * @author Frodez
	 * @date 2018-11-14
	 */
	Result getUserInfo(@NotBlank String userName);

	/**
	 * 根据用户ID批量获取用户信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	Result getUserInfosByIds(@NotEmpty List<Long> userIds);

	/**
	 * 根据用户名批量获取用户信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	Result getUserInfosByNames(@NotEmpty List<String> userNames);

	/**
	 * 根据用户ID更新用户信息
	 * @author Frodez
	 * @date 2019-03-16
	 */
	Result refreshUserInfoByIds(@NotEmpty List<Long> userIds);

	/**
	 * 根据用户名更新用户信息
	 * @author Frodez
	 * @date 2019-03-16
	 */
	Result refreshUserInfoByNames(@NotEmpty List<String> userNames);

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
	 * 添加新权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	Result addPermission(@Valid @NotNull AddPermission param);

	/**
	 * 赋予角色权限
	 * @author Frodez
	 * @date 2019-03-15
	 */
	Result setRolePermission(@Valid @NotNull SetRolePermission param);

}
