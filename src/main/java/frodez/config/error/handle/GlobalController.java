package frodez.config.error.handle;

import frodez.config.error.exception.ServiceException;
import frodez.constant.setting.DefaultResult;
import frodez.util.http.ServletUtil;
import frodez.util.result.ResultEnum;
import frodez.util.result.ResultUtil;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
	 * @date 2018-12-21
	 */
	@ExceptionHandler(value = ServiceException.class)
	public void defaultErrorHandler(HttpServletResponse response, ServiceException e) {
		log.error("[defaultErrorHandler]{}", e.getMessage());
		ServletUtil.writeJson(response, ResultEnum.ERROR_SERVICE.getStatus(), ResultUtil.errorService(e.getMessage())
			.toString());
	}

	/**
	 * 默认异常处理器
	 * @param HttpServletResponse 响应
	 * @param Exception 异常
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@ExceptionHandler(value = Exception.class)
	public void defaultErrorHandler(HttpServletResponse response, Exception e) {
		log.error("[defaultErrorHandler]", e);
		ServletUtil.writeJson(response, ResultEnum.ERROR_SERVICE.getStatus(), DefaultResult.ERROR_SERVICE_STRING);
	}

	/**
	 * 默认异常处理器
	 * @param HttpServletResponse 响应
	 * @param HttpMessageNotReadableException 异常
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public void defaultErrorHandler(HttpServletResponse response, HttpMessageNotReadableException e) {
		log.error("[defaultErrorHandler]{}", e.getMessage());
		ServletUtil.writeJson(response, ResultEnum.ERROR_REQUEST.getStatus(), DefaultResult.ERROR_REQUEST_STRING);
	}

}
