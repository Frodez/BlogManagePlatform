package info.frodez.util.http;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP工具类
 * @author Frodez
 * @date 2018-12-21
 */
@Slf4j
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

	/**
	 * 向response直接写入json
	 * @author Frodez
	 * @date 2019-01-07
	 */
	public static void writeJson(HttpServletResponse response, String json) {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			out = response.getWriter();
			out.append(json);
		} catch (Exception e) {
			log.error("[writeJson]", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
