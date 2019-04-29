package frodez.config.mvc;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * springMVC配置
 * @author Frodez
 * @date 2019-03-14
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//清除掉原本的AbstractJackson2HttpMessageConverter。
		converters.removeIf((iter) -> {
			return iter instanceof AbstractJackson2HttpMessageConverter;
		});
		converters.add(0, new JsonHttpMessageConverer(MediaType.APPLICATION_JSON_UTF8));
	}

}
