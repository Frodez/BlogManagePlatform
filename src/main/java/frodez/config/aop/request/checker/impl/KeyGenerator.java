package frodez.config.aop.request.checker.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import frodez.config.security.settings.SecurityProperties;
import frodez.util.http.HttpUtil;

@Component
public class KeyGenerator {

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;
	
	public String servletKey(String sault, HttpServletRequest request) {
		if (properties.needVerify(request.getRequestURI())) {
			// 非登录接口使用token判断,同一token不能重复请求
			String fullToken = properties.getFullToken(request);
			fullToken = fullToken == null ? "" : fullToken;
			return sault + fullToken;
		} else {
			// 登录接口使用IP判断,同一IP不能重复请求
			String address = HttpUtil.getAddr(request);
			return sault + address;
		}
	}
	
}
