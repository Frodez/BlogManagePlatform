package frodez.controller.user;

import frodez.constant.setting.DefDesc;
import frodez.dao.model.user.Permission;
import frodez.dao.param.user.RolePermissionQuery;
import frodez.dao.result.user.PermissionInfo;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.param.PageQuery;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户权限信息控制器
 * @author Frodez
 * @date 2018-12-01
 */
@RestController
@RequestMapping("/user/permissions")
@Api(tags = "用户权限信息控制器")
public class PermissionController {

	@Autowired
	private IUserService userService;

	/**
	 * 分页查询权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@GetMapping
	@ApiOperation(value = "分页查询权限信息接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = Permission.class) })
	public Result getPermissions(@RequestBody @ApiParam(value = DefDesc.Message.PAGE_QUERY,
		required = true) PageQuery param) {
		return userService.getPermissions(param);
	}

	/**
	 * 根据角色ID获取权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@GetMapping("/byRoleId")
	@ApiOperation(value = "根据角色ID获取权限信息接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PermissionInfo.class) })
	public Result getRolePermissions(@RequestBody @ApiParam(value = "权限信息获取请求参数",
		required = true) RolePermissionQuery param) {
		return userService.getRolePermissions(param);
	}

}
