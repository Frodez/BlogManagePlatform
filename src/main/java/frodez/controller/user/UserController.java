package frodez.controller.user;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.dao.param.user.Doregister;
import frodez.service.user.facade.IAuthorityService;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理控制器")
public class UserController {

	@Autowired
	private IAuthorityService authorityService;

	@Autowired
	private IUserService userService;

	@RepeatLock
	@GetMapping("/self")
	@ApiOperation(value = "查看本用户详情接口")
	public Result getUserDetail() {
		return null;
	}

	@RepeatLock
	@GetMapping("/detail")
	@ApiOperation(value = "查看用户详情接口")
	public Result getUserDetail(Long userId) {
		return authorityService.getUserInfo(userId);
	}

	/**
	 * 注册接口
	 * @author Frodez
	 * @date 2019-02-02
	 */
	@RepeatLock
	@PostMapping("/register")
	@ApiOperation(value = "注册接口")
	public Result register(@RequestBody @ApiParam(value = "用户注册请求参数", required = true) Doregister param) {
		return userService.register(param);
	}

}
