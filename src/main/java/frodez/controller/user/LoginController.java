package frodez.controller.user;

import frodez.config.aop.request.annotation.TimeoutLock;
import frodez.config.swagger.annotation.Success;
import frodez.dao.model.result.login.RefreshInfo;
import frodez.dao.param.login.LoginUser;
import frodez.dao.param.login.RefreshToken;
import frodez.service.user.facade.ILoginService;
import frodez.util.beans.result.Result;
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
@TimeoutLock(3000)
@RestController
@RequestMapping(value = "/login", name = "登录控制器")
public class LoginController {

	@Autowired
	private ILoginService loginService;

	@Success(String.class)
	@PostMapping(value = "/auth", name = "登录接口")
	public Result auth(@RequestBody LoginUser param) {
		return loginService.login(param);
	}

	@PostMapping(value = "/out", name = "登出接口")
	public Result out() {
		return loginService.logout();
	}

	@Success(RefreshInfo.class)
	@PostMapping(value = "/refresh", name = "重新登录接口")
	public Result refresh(@RequestBody RefreshToken param) {
		return loginService.refresh(param);
	}

}
