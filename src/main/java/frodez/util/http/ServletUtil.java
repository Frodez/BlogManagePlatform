package frodez.util.http;

import java.io.PrintWriter;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

/**
 * HTTP工具类
 * @author Frodez
 * @date 2018-12-21
 */
public class ServletUtil {

	/**
	 * 获取真实地址
	 * @param HttpServletRequest 请求
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public static String getAddr(HttpServletRequest request) {
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
	public static void writeJson(HttpServletResponse response, @Nullable HttpStatus status, String json) {
		if (status != null) {
			response.setStatus(status.value());
		}
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			out = response.getWriter();
			out.append(json);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 向response写入text/plain类型数据
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static void writePlainText(HttpServletResponse response, @Nullable HttpStatus status, String text) {
		if (status != null) {
			response.setStatus(status.value());
		}
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain; charset=utf-8");
			out = response.getWriter();
			out.append(text);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 向response写入text/html类型数据
	 * @author Frodez
	 * @date 2019-01-15
	 */
	public static void writeHtml(HttpServletResponse response, @Nullable HttpStatus status, String html) {
		if (status != null) {
			response.setStatus(status.value());
		}
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");
			out = response.getWriter();
			out.append(html);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
