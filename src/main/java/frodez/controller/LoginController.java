package frodez.controller;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.service.user.facade.IUserService;
import frodez.service.user.param.LoginDTO;
import frodez.service.user.param.ReLoginDTO;
import frodez.service.user.param.RegisterDTO;
import frodez.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RestController
@RequestMapping("/login")
@Api(tags = "登录控制器")
public class LoginController {

	/**
	 * 用户服务
	 */
	@Autowired
	private IUserService userService;

	/**
	 * 登录接口
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@RepeatLock
	@PostMapping("/auth")
	@ApiOperation(value = "登录接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 200, message = "失败"),
		@ApiResponse(code = 400, message = "请求参数错误"), @ApiResponse(code = 500, message = "服务器错误") })
	public Result auth(@RequestBody @ApiParam(value = "用户登录请求参数", required = true) LoginDTO param) {
		return userService.login(param);
	}

	/**
	 * 重新登录接口
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@RepeatLock
	@PostMapping("/refresh")
	@ApiOperation(value = "重新登录接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 200, message = "失败"),
		@ApiResponse(code = 400, message = "请求参数错误"), @ApiResponse(code = 500, message = "服务器错误") })
	public Result refresh(@RequestBody @ApiParam(value = "用户重新登录请求参数", required = true) ReLoginDTO param) {
		return userService.reLogin(param);
	}

	/**
	 * 登出接口
	 * @author Frodez
	 * @date 2019-02-19
	 */
	@RepeatLock
	@PostMapping("/out")
	@ApiOperation(value = "登出接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 200, message = "失败"),
		@ApiResponse(code = 400, message = "请求参数错误"), @ApiResponse(code = 500, message = "服务器错误") })
	public Result out() {
		return userService.logout();
	}

	/**
	 * 注册接口
	 * @author Frodez
	 * @date 2019-02-02
	 */
	@RepeatLock
	@PostMapping("/register")
	@ApiOperation(value = "注册接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 200, message = "失败"),
		@ApiResponse(code = 400, message = "请求参数错误"), @ApiResponse(code = 500, message = "服务器错误") })
	public Result register(@RequestBody @ApiParam(value = "用户注册请求参数", required = true) RegisterDTO param) {
		return userService.register(param);
	}

}
