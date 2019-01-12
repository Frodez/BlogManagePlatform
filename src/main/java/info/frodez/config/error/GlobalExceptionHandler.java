package info.frodez.config.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import info.frodez.config.aop.request.RepeatException;
import info.frodez.util.http.HttpUtil;
import info.frodez.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;

/***
 * 统一异常处理*
 * @author Frodez
 * @date 2018-12-05
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 默认异常处理器
	 * @param HttpServletRequest 请求
	 * @param Exception 异常
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@ExceptionHandler(value = Exception.class)
	public void defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
		log.error("[defaultErrorHandler]", e);
		if (e.getCause() instanceof RepeatException) {
			// 如果是重复请求
			HttpUtil.writeJson(response, ResultUtil.REPEAT_REQUEST_STRING);
		} else {
			HttpUtil.writeJson(response, ResultUtil.FAIL_STRING);
		}
	}

}
