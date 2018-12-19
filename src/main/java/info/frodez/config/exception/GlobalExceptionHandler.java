package info.frodez.config.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import info.frodez.util.result.Result;
import info.frodez.util.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理
 * @author Frodez
 * @date 2018-12-05
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Result defaultErrorHandler(HttpServletRequest req, Exception e) {
		log.error("[defaultErrorHandler]", e);
		return new Result(ResultEnum.FAIL);
	}

}
