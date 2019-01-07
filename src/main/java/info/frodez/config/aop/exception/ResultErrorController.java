package info.frodez.config.aop.exception;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import info.frodez.util.http.HttpUtil;
import info.frodez.util.result.ResultUtil;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ResultErrorController extends AbstractErrorController {

	private final ErrorProperties errorProperties;

	@Autowired
	public ResultErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
		super(errorAttributes);
		this.errorProperties = serverProperties.getError();
	}

	@Override
	public String getErrorPath() {
		return errorProperties.getPath();
	}

	@RequestMapping
	public void error(HttpServletResponse response) {
		HttpUtil.writeJson(response, ResultUtil.getFailString());
	}

}
