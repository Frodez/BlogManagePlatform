package frodez.config.aop.request.checker.impl;

import frodez.config.security.util.TokenManager;
import frodez.util.constant.setting.DefStr;
import frodez.util.http.ServletUtil;
import frodez.util.http.URLMatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {

	public static String servletKey(String sault, HttpServletRequest request) {
		StringBuilder builder = new StringBuilder(sault).append(DefStr.SEPERATOR).append(request.getRequestURI());
		if (URLMatcher.needVerify(request.getRequestURI())) {
			// 非登录接口使用token判断,同一token不能重复请求
			String fullToken = TokenManager.getFullToken(request);
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
