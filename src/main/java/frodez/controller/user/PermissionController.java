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

	@GetMapping
	@ApiOperation(value = "查询权限信息接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PermissionDetail.class) })
	public Result getPermission(@RequestParam("id") @ApiParam(value = "权限ID", required = true) Long id) {
		return authorityService.getPermission(id);
	}

	@GetMapping("/page")
	@ApiOperation(value = "分页查询权限信息接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = Permission.class) })
	public Result getPermissions(@RequestBody @ApiParam(value = DefDesc.Message.PAGE_QUERY,
		required = true) QueryPage param) {
		return authorityService.getPermissions(param);
	}

	@GetMapping("/byRoleId")
	@ApiOperation(value = "根据角色ID获取权限信息接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PermissionInfo.class) })
	public Result getRolePermissions(@RequestBody @ApiParam(value = "权限信息获取请求参数",
		required = true) QueryRolePermission param) {
		return authorityService.getRolePermissions(param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除权限接口")
	public Result removePermission(@RequestParam("id") @ApiParam(value = "权限ID", required = true) Long id) {
		try {
			return authorityService.removePermission(id).get();
		} catch (Exception e) {
			return Result.errorService();
		}
	}

	@PostMapping("/add")
	@ApiOperation(value = "添加新权限接口")
	public Result addPermission(@RequestBody @ApiParam(value = "新增权限请求参数", required = true) AddPermission param) {
		try {
			return authorityService.addPermission(param).get();
		} catch (Exception e) {
			return Result.errorService();
		}
	}

	@PostMapping("/update")
	@ApiOperation(value = "修改权限接口")
	public Result updatePermission(@RequestBody @ApiParam(value = "修改权限请求参数", required = true) UpdatePermission param) {
		try {
			return authorityService.updatePermission(param).get();
		} catch (Exception e) {
			return Result.errorService();
		}
	}

}
