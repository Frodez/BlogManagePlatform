package frodez.controller.user;

import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.dao.model.user.Role;
import frodez.dao.param.user.AddRole;
import frodez.dao.param.user.UpdateRole;
import frodez.dao.param.user.UpdateRolePermission;
import frodez.dao.result.user.RoleDetail;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户角色信息控制器
 * @author Frodez
 * @date 2018-12-01
 */
@RestController
@RequestMapping("/role")
@Api(tags = "用户角色信息控制器")
public class RoleController {

	@Autowired
	private IAuthorityService authorityService;

	@GetMapping(name = "查询角色信息接口")
	@Success(RoleDetail.class)
	public Result getRole(@ApiParam("角色ID") Long id) {
		return authorityService.getRole(id);
	}

	@GetMapping(value = "/page", name = "分页查询角色信息接口")
	@Success(value = Role.class, containerType = Container.PAGE)
	public Result getRoles(@RequestBody QueryPage param) {
		return authorityService.getRoles(param);
	}

	@PostMapping(value = "/updatePermission", name = "修改角色权限接口")
	public Result updateRolePermission(@RequestBody UpdateRolePermission param) {
		return authorityService.updateRolePermission(param);
	}

	@DeleteMapping(name = "删除角色接口")
	public Result removeRole(@ApiParam("角色ID") Long id) {
		return authorityService.removeRole(id);
	}

	@PostMapping(value = "/add", name = "添加新角色接口")
	public Result addRole(@RequestBody AddRole param) {
		return authorityService.addRole(param);
	}

	@PostMapping(value = "/update", name = "修改角色接口")
	public Result updateRole(@RequestBody UpdateRole param) {
		return authorityService.updateRole(param);
	}

}
