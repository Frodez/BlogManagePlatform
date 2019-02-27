package frodez.config.aop.request.checker.impl;

import frodez.config.security.settings.SecurityProperties;
import frodez.constant.setting.DefStr;
import frodez.util.http.ServletUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	public String servletKey(String sault, HttpServletRequest request) {
		StringBuilder builder = new StringBuilder(sault).append(DefStr.SEPERATOR).append(request.getRequestURI());
		if (properties.needVerify(request.getRequestURI())) {
			// 非登录接口使用token判断,同一token不能重复请求
			String fullToken = properties.getFullToken(request);
			if (fullToken == null) {
				return builder.toString();
			} else {
				return builder.append(DefStr.SEPERATOR).append(fullToken).toString();
			}
		} else {
			// 登录接口使用IP判断,同一IP不能重复请求
			return builder.append(DefStr.SEPERATOR).append(ServletUtil.getAddr(request)).toString();
		}
	}

}
