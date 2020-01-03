package frodez.controller.user;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.security.util.UserUtil;
import frodez.config.swagger.annotation.Success;
import frodez.dao.model.result.user.UserInfo;
import frodez.dao.param.user.RegisterUser;
import frodez.dao.param.user.UpdatePassword;
import frodez.dao.param.user.UpdateUser;
import frodez.service.user.facade.IUserService;
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
 * 用户控制器
 * @author Frodez
 * @date 2019-12-29
 */
@RepeatLock
@RestController
@RequestMapping(value = "/user", name = "用户控制器")
public class UserController {

	@Autowired
	private IUserService userService;

	@PostMapping(value = "/register", name = "注册接口")
	public Result register(@RequestBody RegisterUser param) {
		return userService.register(param);
	}

	@DeleteMapping(value = "/logoff", name = "注销接口")
	public Result logoff(@ApiParam("用户名") String name, @ApiParam("密码") String password) {
		return userService.logOff(name, password);
	}

	@GetMapping(value = "/self", name = "查看本用户信息接口")
	@Success(UserInfo.class)
	public Result getUserInfo() {
		return Result.success(UserUtil.now());
	}

	@PostMapping(value = "/password/update", name = "更改用户密码信息接口")
	public Result updatePassword(@RequestBody UpdatePassword param) {
		return userService.updatePassword(param);
	}

	@PostMapping(value = "/update", name = "更改用户基本信息接口")
	public Result updateUser(@RequestBody UpdateUser param) {
		return userService.updateUser(param);
	}

}
