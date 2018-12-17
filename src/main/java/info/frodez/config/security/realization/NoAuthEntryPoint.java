package info.frodez.config.security.realization;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import info.frodez.util.result.Result;
import info.frodez.util.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 无验证访问控制
 * @author Frodez
 * @date 2018-11-27
 */
@Slf4j
@Component
public class NoAuthEntryPoint implements AuthenticationEntryPoint {

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8"); 
	    PrintWriter out = null;  
	    try {
	        out = response.getWriter();  
	        out.append(new Result(ResultEnum.NO_AUTH).toString());
	    } catch (IOException e) {  
	        log.error("[commence]", e); 
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    }
    }
	
}
