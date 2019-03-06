package frodez.controller.user;

import frodez.dao.param.user.RolePermissionDTO;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
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
	private IAuthorityService authorityService;

	/**
	 * 根据角色获取权限(可分页)
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@GetMapping("/byRoleId")
	public Result getRolePermissions(@RequestBody RolePermissionDTO param) {
		return authorityService.getRolePermissions(param);
	}

}
