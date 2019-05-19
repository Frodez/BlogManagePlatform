package frodez.config.aop.request.checker.impl;

import frodez.config.security.util.Matcher;
import frodez.config.security.util.TokenUtil;
import frodez.constant.settings.DefStr;
import frodez.util.common.StrUtil;
import frodez.util.http.ServletUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {

	public static String servletKey(String sault, HttpServletRequest request) {
		String key = StrUtil.concat(sault, DefStr.SEPERATOR, request.getRequestURI());
		if (Matcher.needVerify(request.getRequestURI())) {
			// 非登录接口使用token判断,同一token不能重复请求
			String fullToken = TokenUtil.getFullToken(request);
			if (fullToken == null) {
				return key;
			} else {
				return StrUtil.concat(key, DefStr.SEPERATOR, fullToken);
			}
		} else {
			// 登录接口使用IP判断,同一IP不能重复请求
			return StrUtil.concat(key, DefStr.SEPERATOR, ServletUtil.getAddr(request));
		}
	}

}
