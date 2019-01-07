package info.frodez.config.aop.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import info.frodez.config.aop.request.NoRepeatException;
import info.frodez.util.result.ResultUtil;
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
	public void defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
		PrintWriter out = null;
		try {
			log.error("[defaultErrorHandler]", e);
			out = response.getWriter();
			if (e.getCause() instanceof NoRepeatException) {
				// 如果是重复请求
				out = response.getWriter();
				out.append(ResultUtil.getRepeatRequestString());
			} else {
				out.append(ResultUtil.getFailString());
			}
		} catch (IOException ioe) {
			log.error("[commence]", ioe);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
