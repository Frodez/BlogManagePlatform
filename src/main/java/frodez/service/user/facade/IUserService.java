package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.common.NotNullParam;
import frodez.dao.param.user.RolePermissionDTO;
import frodez.util.beans.param.PageDTO;
import frodez.util.beans.result.Result;
import javax.validation.Valid;

/**
 * 用户信息服务
 * @author Frodez
 * @date 2018-11-14
 */
public interface IUserService {

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
	Result getPermissions(@Valid @NotNullParam PageDTO param);

	/**
	 * 根据角色ID获取对应权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getRolePermissions(@Valid @NotNullParam RolePermissionDTO param);

	/**
	 * 分页查询角色信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	Result getRoles(@Valid @NotNullParam PageDTO param);

}
