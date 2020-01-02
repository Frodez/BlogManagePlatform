package frodez.controller.user;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.security.util.UserUtil;
import frodez.config.swagger.annotation.Success;
import frodez.dao.model.result.user.UserInfo;
import frodez.dao.param.user.RegisterUser;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping(value = "/self", name = "查看本用户信息接口")
	@Success(UserInfo.class)
	public Result getUserInfo() {
		return Result.success(UserUtil.now());
	}

}
