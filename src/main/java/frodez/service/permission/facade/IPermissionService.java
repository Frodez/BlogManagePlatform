package frodez.service.permission.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.constant.annotations.decoration.Page;
import frodez.constant.annotations.decoration.ServiceOnly;
import frodez.dao.model.result.permission.PermissionDetail;
import frodez.dao.model.table.permission.Endpoint;
import frodez.dao.model.table.permission.Menu;
import frodez.dao.model.table.permission.Tag;
import frodez.dao.param.permission.UpdateRolePermission;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限信息服务<br>
 * 用途:<br>
 * 1.查看菜单权限,标签权限列表<br>
 * 2.查看权限详细信息<br>
 * 3.为角色查看和分配权限<br>
 * @author Frodez
 * @date 2018-11-14
 */
public interface IPermissionService {

	/**
	 * 分页查询菜单权限
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Page
	@Success(value = Menu.class, containerType = Container.PAGE)
	Result getMenuPermissions(@Valid @NotNull QueryPage query);

	/**
	 * 分页查询标签权限
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Page
	@Success(value = Tag.class, containerType = Container.PAGE)
	Result getTagPermissions(@Valid @NotNull QueryPage query);

	/**
	 * 根据角色ID获取角色权限对应的所有端点信息
	 * @author Frodez
	 * @date 2019-12-30
	 */
	@Check
	@ServiceOnly
	@Success(value = Endpoint.class, containerType = Container.LIST)
	Result getEndpoints(@NotNull Long roleId);

	/**
	 * 根据角色ID查询角色权限详细信息
	 * @author Frodez
	 * @date 2019-12-30
	 */
	@Check
	@Success(PermissionDetail.class)
	Result getPermission(@NotNull Long roleId);

	/**
	 * 更新角色权限信息
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Check
	@Transactional
	Result updateRolePermission(@Valid @NotNull UpdateRolePermission param);

}
