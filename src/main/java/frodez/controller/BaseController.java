package frodez.controller;

import frodez.dao.result.user.UserInfo;
import frodez.util.login.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseController {

	@Autowired
	private UserUtil userUtil;

	public UserInfo getInfo() {
		return userUtil.getInfo();
	}

}
