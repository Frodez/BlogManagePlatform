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
		if(address == null) {
			return request.getRemoteAddr();
		}
		return address;
	}

}
