package frodez.util.http;

import frodez.util.beans.result.Result;
import frodez.util.common.EmptyUtil;
import java.io.PrintWriter;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

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
		if (EmptyUtil.yes(address) || "unknown".equalsIgnoreCase(address)) {
			address = request.getHeader("Proxy-Client-address");
		} else {
			return address;
		}
		if (EmptyUtil.yes(address) || "unknown".equalsIgnoreCase(address)) {
			address = request.getHeader("WL-Proxy-Client-address");
		} else {
			return address;
		}
		if (EmptyUtil.yes(address) || "unknown".equalsIgnoreCase(address)) {
			address = request.getHeader("HTTP_CLIENT_address");
		} else {
			return address;
		}
		if (EmptyUtil.yes(address) || "unknown".equalsIgnoreCase(address)) {
			address = request.getHeader("HTTP_X_FORWARDED_FOR");
		} else {
			return address;
		}
		if (EmptyUtil.yes(address) || "unknown".equalsIgnoreCase(address)) {
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
	public static void writeJson(HttpServletResponse response, Result result) {
		Assert.notNull(result, "result不能为空!");
		if (response.isCommitted()) {
			return;
		}
		response.setStatus(result.httpStatus().value());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(result.toString());
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 向response直接写入json
	 * @author Frodez
	 * @date 2019-01-07
	 */
	public static void writeJson(HttpServletResponse response, @Nullable HttpStatus status, String json) {
		if (response.isCommitted()) {
			return;
		}
		if (status != null) {
			response.setStatus(status.value());
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(json);
			out.flush();
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
		if (response.isCommitted()) {
			return;
		}
		if (status != null) {
			response.setStatus(status.value());
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(text);
			out.flush();
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
		if (response.isCommitted()) {
			return;
		}
		if (status != null) {
			response.setStatus(status.value());
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(html);
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
