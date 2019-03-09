package frodez.service.user.facade;

import frodez.dao.param.user.RolePermissionQuery;
import frodez.util.beans.param.PageQuery;
import frodez.util.beans.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 用户信息服务
 * @author Frodez
 * @date 2018-11-14
 */
public interface IUserService {

	/**
	 * 根据用户ID查询用户信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getUserInfo(@Valid @NotNull Long userId);

	/**
	 * 添加角色
	 * @author Frodez
	 * @date 2019-02-02
	 */
	Result addRole();

	/**
	 * 分页查询权限信息
	 * @author Frodez
	 * @date 2018-03-06
	 */
	Result getPermissions(@Valid @NotNull PageQuery param);

	/**
	 * 根据角色ID获取对应权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getRolePermissions(@Valid @NotNull RolePermissionQuery param);

	/**
	 * 分页查询角色信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getRoles(@Valid @NotNull PageQuery param);

}
