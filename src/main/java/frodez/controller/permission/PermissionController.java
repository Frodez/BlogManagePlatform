package frodez.controller.permission;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.dao.model.result.permission.PermissionDetail;
import frodez.dao.model.table.permission.Menu;
import frodez.dao.model.table.permission.Tag;
import frodez.dao.param.permission.UpdateRolePermission;
import frodez.service.permission.facade.IPermissionService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限控制器
 * @author Frodez
 * @date 2020-01-01
 */
@RepeatLock
@RestController
@RequestMapping(value = "/permission", name = "权限控制器")
public class PermissionController {

	@Autowired
	private IPermissionService permissionService;

	@GetMapping(value = "/page/menu", name = "分页查询菜单权限信息接口")
	@Success(value = Menu.class, containerType = Container.PAGE)
	public Result getMenuPermissions(@RequestBody QueryPage query) {
		return permissionService.getMenuPermissions(query);
	}

	@GetMapping(value = "/page/tag", name = "分页查询标签权限信息接口")
	@Success(value = Tag.class, containerType = Container.PAGE)
	public Result getTagPermissions(@RequestBody QueryPage query) {
		return permissionService.getTagPermissions(query);
	}

	@GetMapping(value = "/detail", name = "角色ID查询角色权限详细信息接口")
	@Success(PermissionDetail.class)
	public Result getPermission(@ApiParam("角色ID") Long roleId) {
		return permissionService.getPermission(roleId);
	}

	@PostMapping(value = "/update", name = "更新角色权限信息接口")
	public Result setRole(@RequestBody UpdateRolePermission param) {
		return permissionService.updateRolePermission(param);
	}

}
