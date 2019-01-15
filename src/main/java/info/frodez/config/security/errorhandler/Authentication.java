package info.frodez.config.security.errorhandler;

import info.frodez.util.http.HttpUtil;
import info.frodez.util.result.ResultUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 无验证访问控制
 * @author Frodez
 * @date 2018-11-27
 */
@Component
public class Authentication implements AuthenticationEntryPoint {

	/**
	 * 如果捕获到的是 AuthenticationException,那么将会使用其对应的AuthenticationEntryPoint的 commence()处理.<br>
	 * 如果捕获的异常是一个AccessDeniedException,那么将视当前访问的用户是否已经登录认证做不同的处理.<br>
	 * 如果未登录,则会使用关联的AuthenticationEntryPoint的commence()方法进行处理.<br>
	 * 否则将使用关联的AccessDeniedHandler的handle()方法进行处理.
	 * @author Frodez
	 * @date 2018-12-22
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		HttpUtil.writeJson(response, ResultUtil.NO_AUTH_STRING);
	}

}
