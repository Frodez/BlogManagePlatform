package info.frodez.config.aop.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import info.frodez.config.aop.request.NoRepeatException;
import info.frodez.util.result.Result;
import info.frodez.util.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理
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
	public Result defaultErrorHandler(HttpServletRequest request, Exception e) {
		log.error("[defaultErrorHandler]", e);
		if (e.getCause() instanceof NoRepeatException) {
			// 如果是重复请求
			return new Result(ResultEnum.REPEAT_REQUEST);
		}
		return new Result(ResultEnum.FAIL);
	}

}
