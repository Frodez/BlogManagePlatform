package frodez.controller.user;

import frodez.dao.model.user.Role;
import frodez.dao.param.user.AddRole;
import frodez.dao.param.user.UpdateRole;
import frodez.dao.param.user.UpdateRolePermission;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.constant.setting.DefDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	/**
	 * 分页查询角色信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@GetMapping
	@ApiOperation(value = "分页查询角色信息接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = Role.class) })
	public Result getRoles(@RequestBody @ApiParam(value = DefDesc.Message.PAGE_QUERY,
		required = true) QueryPage param) {
		return authorityService.getRoles(param);
	}

	@PostMapping("/updatePermission")
	@ApiOperation(value = "修改角色权限接口")
	public Result updateRolePermission(@RequestBody @ApiParam(value = "修改角色权限请求参数",
		required = true) UpdateRolePermission param) {
		return authorityService.updateRolePermission(param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除角色接口")
	public Result removeRole(@RequestParam("id") @ApiParam(value = "角色ID", required = true) Long id) {
		return authorityService.removeRole(id);
	}

	@PostMapping("/add")
	@ApiOperation(value = "添加新角色接口")
	public Result addRole(@RequestBody @ApiParam(value = "新增角色请求参数", required = true) AddRole param) {
		return authorityService.addRole(param);
	}

	@PostMapping("/update")
	@ApiOperation(value = "修改角色接口")
	public Result updateRole(@RequestBody @ApiParam(value = "修改角色请求参数", required = true) UpdateRole param) {
		return authorityService.updateRole(param);
	}

}
