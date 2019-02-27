package frodez.config.security.error;

import frodez.constant.setting.DefResult;
import frodez.util.http.ServletUtil;
import frodez.util.result.ResultEnum;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 无权限访问控制
 * @author Frodez
 * @date 2018-11-27
 */
@Component
public class AccessDenied implements AccessDeniedHandler {

	/**
	 * 如果捕获到的是 AuthenticationException,那么将会使用其对应的AuthenticationEntryPoint的 commence()处理.<br>
	 * 如果捕获的异常是一个AccessDeniedException,那么将视当前访问的用户是否已经登录认证做不同的处理.<br>
	 * 如果未登录,则会使用关联的AuthenticationEntryPoint的commence()方法进行处理.<br>
	 * 否则将使用关联的AccessDeniedHandler的handle()方法进行处理.
	 * @author Frodez
	 * @date 2018-12-22
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		ServletUtil.writeJson(response, ResultEnum.NO_ACCESS.getStatus(), DefResult.NO_ACCESS_STRING);
	}

}
