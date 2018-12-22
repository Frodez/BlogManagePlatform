package info.frodez.util.http;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP工具类
 * @author Frodez
 * @date 2018-12-21
 */
public class HttpUtil {

	/**
	 * 获取真实地址
	 * @param HttpServletRequest 请求
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public static String getRealAddr(HttpServletRequest request) {
		String address = request.getHeader("x-forwarded-for");
		if (address == null || address.length() == 0 || "unknown".equalsIgnoreCase(address)) {
			address = request.getHeader("Proxy-Client-address");
		} else {
			return address;
		}
		if (address == null || address.length() == 0 || "unknown".equalsIgnoreCase(address)) {
			address = request.getHeader("WL-Proxy-Client-address");
		} else {
			return address;
		}
		if (address == null || address.length() == 0 || "unknown".equalsIgnoreCase(address)) {
			address = request.getHeader("HTTP_CLIENT_address");
		} else {
			return address;
		}
		if (address == null || address.length() == 0 || "unknown".equalsIgnoreCase(address)) {
			address = request.getHeader("HTTP_X_FORWARDED_FOR");
		} else {
			return address;
		}
		if (address == null || address.length() == 0 || "unknown".equalsIgnoreCase(address)) {
			address = request.getRemoteAddr();
		} else {
			return address;
		}
		return address;
	}

}
