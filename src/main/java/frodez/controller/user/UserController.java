package frodez.controller.user;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.security.util.UserUtil;
import frodez.dao.param.user.Doregister;
import frodez.service.user.facade.IAuthorityService;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	@GetMapping("/info/self")
	@ApiOperation(value = "查看本用户信息接口")
	public Result getUserInfo() {
		return Result.success(UserUtil.get());
	}

	@RepeatLock
	@GetMapping("/info/byId")
	@ApiOperation(value = "查看用户信息接口")
	public Result getUserInfoById(@RequestParam("userId") @ApiParam(value = "用户ID", required = true) Long userId) {
		return authorityService.getUserInfo(userId);
	}

	@RepeatLock
	@GetMapping("/info/byName")
	@ApiOperation(value = "查看用户信息接口")
	public Result getUserInfoByName(@RequestParam("userName") @ApiParam(value = "用户名",
		required = true) String userName) {
		return authorityService.getUserInfo(userName);
	}

	@RepeatLock
	@GetMapping("/infos/byId")
	@ApiOperation(value = "批量查看用户信息接口")
	public Result getUserInfosById(@RequestParam("userId") @ApiParam(value = "用户ID", required = true) List<
		Long> userIds) {
		return authorityService.getUserInfosByIds(userIds, false);
	}

	@RepeatLock
	@GetMapping("/infos/byName")
	@ApiOperation(value = "批量查看用户信息接口")
	public Result getUserInfosByName(@RequestParam("userName") @ApiParam(value = "用户名", required = true) List<
		String> userNames) {
		return authorityService.getUserInfosByNames(userNames, false);
	}

	@RepeatLock
	@PostMapping("/register")
	@ApiOperation(value = "注册接口")
	public Result register(@RequestBody @ApiParam(value = "用户注册请求参数", required = true) Doregister param) {
		return userService.register(param);
	}

}
