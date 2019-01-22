package frodez.controller;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.security.login.BaseController;
import frodez.constant.redis.Repeat;
import frodez.dao.param.user.LoginDTO;
import frodez.service.user.IUserService;
import frodez.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录控制器
 * @author Frodez
 * @date 2018-12-01
 */
@Api
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {

	/**
	 * 用户授权服务
	 */
	@Autowired
	private IUserService authorityService;

	/**
	 * 登录接口
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@ApiOperation(value = "")
	@RepeatLock(Repeat.Login.AUTH)
	@PostMapping("/auth")
	public Result auth(@RequestBody LoginDTO param) {
		return authorityService.login(param);
	}

	// public Result register() {
	//
	// }

}
