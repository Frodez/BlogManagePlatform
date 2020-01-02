package frodez.controller.user;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.dao.model.table.user.Role;
import frodez.dao.param.user.CreateRole;
import frodez.dao.param.user.UpdateRole;
import frodez.service.user.facade.IRoleService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色控制器
 * @author Frodez
 * @date 2020-01-01
 */
@RepeatLock
@RestController
@RequestMapping(value = "/role", name = "角色控制器")
public class RoleController {

	@Autowired
	private IRoleService roleService;

	@GetMapping(value = "/page", name = "分页查询角色信息接口")
	@Success(value = Role.class, containerType = Container.PAGE)
	public Result getRoles(@RequestBody QueryPage query) {
		return roleService.getRoles(query);
	}

	@PostMapping(value = "/add", name = "创建新角色接口")
	public Result addRole(@RequestBody CreateRole param) {
		return roleService.createRole(param);
	}

	@PostMapping(value = "/update", name = "更新角色接口")
	public Result updateRole(@RequestBody UpdateRole param) {
		return roleService.updateRole(param);
	}

	@DeleteMapping(value = "/update", name = "更新角色接口")
	public Result deleteRole(@ApiParam("角色ID") Long roleId) {
		return roleService.deleteRole(roleId);
	}

}
