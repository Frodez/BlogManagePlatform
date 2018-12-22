package info.frodez.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import info.frodez.config.aop.request.NoRepeat;
import info.frodez.constant.request.NoRepeatKey;
import info.frodez.dao.param.user.LoginDTO;
import info.frodez.service.user.IUserAuthorityService;
import info.frodez.util.result.Result;
import info.frodez.util.result.ResultEnum;
import info.frodez.util.validation.ValidationUtil;

/**
 * 登录控制器
 * @author Frodez
 * @date 2018-12-01
 */
@RestController
@RequestMapping(value = "/login")
public class LoginController {

	/**
	 * spring security验证管理器
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * 用户授权服务
	 */
	@Autowired
	private IUserAuthorityService authorityService;

	/**
	 * 登录接口
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@NoRepeat(name = NoRepeatKey.Login.AUTH)
	@RequestMapping(value = "/auth")
	public Result auth(@RequestBody LoginDTO param) {
		String msg = ValidationUtil.validate(param);
		if(!StringUtils.isBlank(msg)) {
			return new Result(ResultEnum.FAIL, msg);
		}
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authorityService.login(param);
	}

	//	public Result register() {
	//
	//	}

}
