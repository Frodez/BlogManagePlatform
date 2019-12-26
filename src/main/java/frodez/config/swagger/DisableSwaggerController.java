package frodez.config.swagger;

import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Profile({ "release", "prod" })
public class DisableSwaggerController {

	@GetMapping(value = { "/swagger-resources/**", "/swagger-ui.html**", "/webjars/**", "/v2/api-docs" })
	public void disable(HttpServletResponse httpResponse) {
		httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
	}

}
