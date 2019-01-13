package info.frodez.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import info.frodez.config.aop.request.ReLock;
import info.frodez.constant.redis.Repeat;
import info.frodez.dao.param.user.LoginDTO;
import info.frodez.service.user.IUserService;
import info.frodez.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 登录控制器
 * @author Frodez
 * @date 2018-12-01
 */
@Api
@RestController
@RequestMapping("/login")
public class LoginController {

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
	@ReLock(Repeat.Login.AUTH)
	@PostMapping("/auth")
	public Result auth(@RequestBody LoginDTO param) {
		return authorityService.login(param);
	}

	// public Result register() {
	//
	// }

}
