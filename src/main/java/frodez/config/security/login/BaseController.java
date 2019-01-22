package frodez.config.security.login;

import frodez.dao.result.user.UserInfo;
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
