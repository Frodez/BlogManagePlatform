package frodez.config.result;

import frodez.util.beans.result.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 正常返回信息处理
 * @author Frodez
 * @date 2019-02-02
 */
@ControllerAdvice
public class StatusCodeProcessor implements ResponseBodyAdvice<Result> {

	/**
	 * 设置适用范围
	 * @author Frodez
	 * @date 2019-02-02
	 */
	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.getMethod().getReturnType() == Result.class;
	}

	/**
	 * 返回前设置http状态码
	 * @author Frodez
	 * @date 2019-02-02
	 */
	@Override
	public Result beforeBodyWrite(Result body, MethodParameter returnType, MediaType selectedContentType, Class<
		? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
		ServerHttpResponse response) {
		response.setStatusCode(body.httpStatus());
		return body;
	}

}
