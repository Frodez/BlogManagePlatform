package frodez.controller.user;

import frodez.dao.model.user.Permission;
import frodez.dao.param.user.AddPermission;
import frodez.dao.param.user.QueryRolePermission;
import frodez.dao.param.user.UpdatePermission;
import frodez.dao.result.user.PermissionDetail;
import frodez.dao.result.user.PermissionInfo;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户权限信息控制器
 * @author Frodez
 * @date 2018-12-01
 */
@RestController
@RequestMapping("/permission")
@Api(tags = "用户权限信息控制器")
public class PermissionController {

	@Autowired
	private IAuthorityService authorityService;

	@GetMapping(name = "查询权限信息接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PermissionDetail.class) })
	public Result getPermission(@ApiParam(value = "权限ID") Long id) {
		return authorityService.getPermission(id);
	}

	@GetMapping(value = "/page", name = "分页查询权限信息接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = Permission.class) })
	public Result getPermissions(@RequestBody QueryPage param) {
		return authorityService.getPermissions(param);
	}

	@GetMapping(value = "/byRoleId", name = "根据角色ID获取权限信息接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PermissionInfo.class) })
	public Result getRolePermissions(@RequestBody QueryRolePermission param) {
		return authorityService.getRolePermissions(param);
	}

	@DeleteMapping(name = "删除权限接口")
	public Result removePermission(@ApiParam(value = "权限ID") Long id) {
		return authorityService.removePermission(id);
	}

	@PostMapping(value = "/add", name = "添加新权限接口")
	public Result addPermission(@RequestBody AddPermission param) {
		return authorityService.addPermission(param);
	}

	@PostMapping(value = "/update", name = "修改权限接口")
	public Result updatePermission(@RequestBody UpdatePermission param) {
		return authorityService.updatePermission(param);
	}

}
