package frodez.util.http;

import frodez.constant.settings.DefCharset;
import frodez.util.beans.result.Result;
import frodez.util.common.EmptyUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Servlet工具类
 * @author Frodez
 * @date 2018-12-21
 */
@Slf4j
@UtilityClass
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
	 * 向response直接写入json,编码为UTF-8
	 * @author Frodez
	 * @throws IOException
	 * @date 2019-01-07
	 */
	@SneakyThrows
	public static void writeJson(HttpServletResponse response, Result result) {
		Assert.notNull(result, "result must not be null");
		if (response.isCommitted()) {
			log.warn("this response has been committed!");
			return;
		}
		response.setStatus(result.httpStatus().value());
		response.setCharacterEncoding(DefCharset.UTF_8);
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		@Cleanup ServletOutputStream stream = response.getOutputStream();
		stream.write(result.jsonBytes());
	}

	/**
	 * 向response直接写入json,编码为UTF-8
	 * @author Frodez
	 * @throws IOException
	 * @date 2019-01-07
	 */
	@SneakyThrows
	public static void writeJson(HttpServletResponse response, String json, @Nullable HttpStatus status) {
		Assert.notNull(json, "json must not be null");
		if (response.isCommitted()) {
			log.warn("this response has been committed!");
			return;
		}
		if (status != null) {
			response.setStatus(status.value());
		}
		response.setCharacterEncoding(DefCharset.UTF_8);
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		@Cleanup PrintWriter out = response.getWriter();
		out.append(json);
		out.flush();
	}

	/**
	 * 向response写入text/plain类型数据,编码为UTF-8
	 * @author Frodez
	 * @throws IOException
	 * @date 2019-01-15
	 */
	@SneakyThrows
	public static void writePlainText(HttpServletResponse response, String text, @Nullable HttpStatus status) {
		Assert.notNull(text, "text must not be null");
		if (response.isCommitted()) {
			log.warn("this response has been committed!");
			return;
		}
		if (status != null) {
			response.setStatus(status.value());
		}
		response.setCharacterEncoding(DefCharset.UTF_8);
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		@Cleanup PrintWriter out = response.getWriter();
		out.append(text);
		out.flush();
	}

	/**
	 * 向response写入text/html类型数据,编码为UTF-8
	 * @author Frodez
	 * @throws IOException
	 * @date 2019-01-15
	 */
	@SneakyThrows
	public static void writeHtml(HttpServletResponse response, String html, @Nullable HttpStatus status) {
		Assert.notNull(html, "html must not be null");
		if (response.isCommitted()) {
			log.warn("this response has been committed!");
			return;
		}
		if (status != null) {
			response.setStatus(status.value());
		}
		response.setCharacterEncoding(DefCharset.UTF_8);
		response.setContentType(MediaType.TEXT_HTML_VALUE);
		@Cleanup PrintWriter out = response.getWriter();
		out.append(html);
		out.flush();
	}

}
