package frodez.controller.user;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.security.util.UserUtil;
import frodez.dao.param.user.Doregister;
import frodez.service.user.facade.IAuthorityService;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
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
	@GetMapping(value = "/info/self", name = "查看本用户信息接口")
	public Result getUserInfo() {
		return Result.success(UserUtil.get());
	}

	@RepeatLock
	@GetMapping(value = "/info/byId", name = "查看用户信息接口")
	public Result getUserInfoById(@RequestParam("userId") @ApiParam(value = "用户ID") Long userId) {
		return authorityService.getUserInfo(userId);
	}

	@RepeatLock
	@GetMapping(value = "/info/byName", name = "查看用户信息接口")
	public Result getUserInfoByName(@RequestParam("userName") @ApiParam(value = "用户名") String userName) {
		return authorityService.getUserInfo(userName);
	}

	@RepeatLock
	@GetMapping(value = "/info/byIds", name = "批量查看用户信息接口")
	public Result getUserInfosById(@RequestBody @ApiParam(value = "用户ID") List<Long> userIds) {
		return authorityService.getUserInfosByIds(userIds, false);
	}

	@RepeatLock
	@GetMapping(value = "/info/byNames", name = "批量查看用户信息接口")
	public Result getUserInfosByName(@RequestBody @ApiParam(value = "用户名") List<String> userNames) {
		return authorityService.getUserInfosByNames(userNames, false);
	}

	@RepeatLock
	@PostMapping(value = "/register", name = "注册接口")
	public Result register(@RequestBody Doregister param) {
		return userService.register(param);
	}

}
