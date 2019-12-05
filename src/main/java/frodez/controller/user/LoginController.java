package frodez.controller.user;

import frodez.config.aop.request.annotation.TimeoutLock;
import frodez.config.swagger.annotation.Success;
import frodez.dao.param.user.DoLogin;
import frodez.dao.param.user.DoRefresh;
import frodez.dao.result.user.UserInfo;
import frodez.service.user.facade.IAuthorityService;
import frodez.service.user.facade.ILoginService;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

	@Autowired
	private ILoginService loginService;

	@Autowired
	private IAuthorityService authorityService;

	@TimeoutLock(3000)
	@PostMapping(value = "/auth", name = "登录接口")
	public Result auth(@RequestBody DoLogin param) {
		return loginService.login(param);
	}

	@TimeoutLock(3000)
	@PostMapping(value = "/refresh", name = "重新登录接口")
	public Result refresh(@RequestBody DoRefresh param) {
		return loginService.refresh(param);
	}

	@TimeoutLock(3000)
	@PostMapping(value = "/out", name = "登出接口")
	public Result out() {
		return loginService.logout();
	}

	@GetMapping(value = "/test", name = "测试用接口")
	@Success(UserInfo.class)
	public Result test(@ApiParam("用户名") String userName) {
		return authorityService.getUserInfo(userName);
	}

}
