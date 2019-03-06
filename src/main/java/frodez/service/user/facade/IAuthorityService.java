package frodez.service.user.facade;

import frodez.util.beans.param.PageDTO;
import frodez.util.beans.result.Result;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 权限信息服务
 * @author Frodez
 * @date 2018-11-14
 */
public interface IAuthorityService {

	/**
	 * 添加角色
	 * @author Frodez
	 * @date 2019-02-02
	 */
	Result addRole();

	/**
	 * 根据用户名获取用户授权信息
	 * @author Frodez
	 * @date 2018-11-14
	 */
	Result getUserInfo(@NotBlank(message = "用户名不能为空!") String userName);

	/**
	 * 获取所有权限信息
	 * @author Frodez
	 * @date 2018-12-04
	 */
	Result getAllPermissions();

	/**
	 * 获取所有角色信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getAllRoles();

	/**
	 * 根据角色ID获取对应权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getRolePermissions(@NotNull(message = "角色ID不能为空!") Long roleId, @Valid @Nullable PageDTO page);

}
