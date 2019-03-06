package frodez.controller.user;

import frodez.dao.param.user.RolePermissionDTO;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.param.PageDTO;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
	public Result getPermissions(@RequestBody @ApiParam(value = PageDTO.DEFAULT_DESC, required = true) PageDTO param) {
		return userService.getPermissions(param);
	}

	/**
	 * 根据角色ID获取权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@GetMapping("/byRoleId")
	@ApiOperation(value = "根据角色ID获取权限信息接口")
	public Result getRolePermissions(@RequestBody @ApiParam(value = "权限信息获取请求参数",
		required = true) RolePermissionDTO param) {
		return userService.getRolePermissions(param);
	}

}
