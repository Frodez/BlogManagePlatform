package frodez.config.aop.request.checker.impl;

import frodez.config.security.util.TokenUtil;
import frodez.util.constant.setting.DefStr;
import frodez.util.http.ServletUtil;
import frodez.util.http.URLMatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {

	public static String servletKey(String sault, HttpServletRequest request) {
		String key = sault.concat(DefStr.SEPERATOR).concat(request.getRequestURI());
		if (URLMatcher.needVerify(request.getRequestURI())) {
			// 非登录接口使用token判断,同一token不能重复请求
			String fullToken = TokenUtil.getFullToken(request);
			if (fullToken == null) {
				return key;
			} else {
				return key.concat(DefStr.SEPERATOR).concat(fullToken);
			}
		} else {
			// 登录接口使用IP判断,同一IP不能重复请求
			return key.concat(DefStr.SEPERATOR).concat(ServletUtil.getAddr(request));
		}
	}

}
