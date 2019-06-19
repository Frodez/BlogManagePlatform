package frodez.config.mvc.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import frodez.constant.errors.code.ServiceException;
import frodez.util.beans.result.Result;
import frodez.util.http.ServletUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

/***
 * 错误信息处理
 * @author Frodez
 * @date 2018-12-05
 */
@Slf4j
@ControllerAdvice
public class GlobalController {

	/**
	 * 默认异常处理器
	 * @param ServiceException 异常
	 * @author Frodez
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @date 2018-12-21
	 */
	@ExceptionHandler(value = ServiceException.class)
	public void serviceExceptionHandler(HttpServletResponse response, ServiceException e)
		throws JsonProcessingException, IOException {
		log.error("[frodez.config.mvc.error.GlobalController.serviceExceptionHandler]{}", e.getMessage());
		ServletUtil.writeJson(response, Result.errorService(e));
	}

	/**
	 * 默认异常处理器
	 * @param HttpServletResponse 响应
	 * @param Exception 异常
	 * @author Frodez
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @date 2018-12-21
	 */
	@ExceptionHandler(value = Exception.class)
	public void exceptionHandler(HttpServletResponse response, Exception e) throws JsonProcessingException,
		IOException {
		log.error("[frodez.config.mvc.error.GlobalController.exceptionHandler]", e);
		ServletUtil.writeJson(response, Result.errorService(e));
	}

	/**
	 * 默认异常处理器
	 * @param HttpServletResponse 响应
	 * @param HttpMessageConversionException 异常
	 * @author Frodez
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @date 2018-12-21
	 */
	@ExceptionHandler(value = HttpMessageConversionException.class)
	public void httpMessageConversionExceptionHandler(HttpServletResponse response, HttpMessageConversionException e)
		throws JsonProcessingException, IOException {
		log.error("[frodez.config.mvc.error.GlobalController.httpMessageConversionExceptionHandler]{}", e.getMessage());
		ServletUtil.writeJson(response, Result.errorRequest());
	}

	/**
	 * 默认异常处理器
	 * @param HttpServletResponse 响应
	 * @param ServletRequestBindingException 异常
	 * @author Frodez
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @date 2018-12-21
	 */
	@ExceptionHandler(value = ServletRequestBindingException.class)
	public void servletRequestBindingExceptionHandler(HttpServletResponse response, ServletRequestBindingException e)
		throws JsonProcessingException, IOException {
		log.error("[frodez.config.mvc.error.GlobalController.servletRequestBindingExceptionHandler]{}", e.getMessage());
		ServletUtil.writeJson(response, Result.errorRequest());
	}

	/**
	 * 默认异常处理器
	 * @param HttpServletResponse 响应
	 * @param ServletRequestBindingException 异常
	 * @author Frodez
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @date 2018-12-21
	 */
	@ExceptionHandler(value = AsyncRequestTimeoutException.class)
	public void asyncRequestTimeoutExceptionHandler(HttpServletResponse response, AsyncRequestTimeoutException e)
		throws JsonProcessingException, IOException {
		log.error("[frodez.config.mvc.error.GlobalController.asyncRequestTimeoutExceptionHandler]{}");
		ServletUtil.writeJson(response, Result.busy());
	}

}
