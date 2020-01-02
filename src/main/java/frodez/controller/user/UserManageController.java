package frodez.controller.user;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.dao.model.result.user.UserBaseInfo;
import frodez.dao.model.result.user.UserDetail;
import frodez.dao.model.result.user.UserInfo;
import frodez.dao.param.user.UpdateUserRole;
import frodez.service.user.facade.IUserManageService;
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
 * 用户管理控制器
 * @author Frodez
 * @date 2018-12-01
 */
@RepeatLock
@RestController
@RequestMapping(value = "/user/manage", name = "用户管理控制器")
public class UserManageController {

	@Autowired
	private IUserManageService userManageService;

	@GetMapping(value = "/page", name = "分页查询用户信息接口")
	@Success(value = UserBaseInfo.class, containerType = Container.PAGE)
	public Result getUsers(@RequestBody QueryPage query) {
		return userManageService.getUsers(query);
	}

	@GetMapping(value = "/byId", name = "根据用户ID查看用户信息接口")
	@Success(UserInfo.class)
	public Result getUserInfoById(@ApiParam("用户ID") Long userId) {
		return userManageService.getUserInfo(userId);
	}

	@GetMapping(value = "/byName", name = "根据用户名查看用户信息接口")
	@Success(value = UserInfo.class, containerType = Container.PAGE)
	public Result getUserInfosById(@ApiParam("用户名") String userName) {
		return userManageService.getUserInfo(userName);
	}

	@GetMapping(value = "/detail/byId", name = "根据用户ID查看用户详细信息接口")
	@Success(UserDetail.class)
	public Result getUserDetail(@ApiParam("用户ID") Long userId) {
		return userManageService.getUserDetail(userId);
	}

	@GetMapping(value = "/detail/byName", name = "根据用户名查看用户详细信息接口")
	@Success(value = UserDetail.class, containerType = Container.PAGE)
	public Result getUserDetail(@ApiParam("用户名") String userName) {
		return userManageService.getUserDetail(userName);
	}

	@PostMapping(value = "/role/set", name = "为用户赋予新角色信息接口")
	public Result setRole(@RequestBody UpdateUserRole param) {
		return userManageService.setRole(param);
	}

	@PostMapping(value = "/role/replace", name = "为某种角色的用户赋予新角色信息接口")
	public Result replaceRole(@ApiParam("前角色ID") Long former, @ApiParam("新角色ID") Long latter) {
		return userManageService.replaceRole(former, latter);
	}

}
