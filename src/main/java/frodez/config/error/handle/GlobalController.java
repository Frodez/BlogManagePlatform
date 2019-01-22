package frodez.config.error.handle;

import frodez.config.error.exception.ServiceException;
import frodez.util.http.HttpUtil;
import frodez.util.result.Result;
import frodez.util.result.ResultUtil;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/***
 * 统一异常处理*
 * @author Frodez
 * @date 2018-12-05
 */
@Slf4j
@RestControllerAdvice
public class GlobalController {

	/**
	 * 默认异常处理器
	 * @param ServiceException 异常
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@ExceptionHandler(value = ServiceException.class)
	public Result defaultErrorHandler(ServiceException e) {
		log.error("[defaultErrorHandler]", e);
		return ResultUtil.fail(e.getMessage());
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
		HttpUtil.writeJson(response, ResultUtil.FAIL_STRING);
	}

}
