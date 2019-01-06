package info.frodez.config.security.impl.error;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import info.frodez.util.result.Result;
import info.frodez.util.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 无验证访问控制
 * @author Frodez
 * @date 2018-11-27
 */
@Slf4j
@Component
public class NoAuthEntryPoint implements AuthenticationEntryPoint {

	/**
	 * 如果捕获到的是 AuthenticationException,那么将会使用其对应的AuthenticationEntryPoint的 commence()处理.<br>
	 * 如果捕获的异常是一个AccessDeniedException,那么将视当前访问的用户是否已经登录认证做不同的处理.<br>
	 * 如果未登录,则会使用关联的AuthenticationEntryPoint的commence()方法进行处理.<br>
	 * 否则将使用关联的AccessDeniedHandler的handle()方法进行处理.
	 * @author Frodez
	 * @date 2018-12-22
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(new Result(ResultEnum.NO_AUTH).toString());
		} catch (IOException e) {
			log.error("[commence]", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
