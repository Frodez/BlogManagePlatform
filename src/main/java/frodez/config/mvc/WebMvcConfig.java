package frodez.config.mvc;

import java.util.List;
import java.util.stream.Collectors;
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
		converters = converters.stream().filter((iter) -> {
			return !(iter instanceof AbstractJackson2HttpMessageConverter);
		}).collect(Collectors.toList());
		converters.add(0, new JsonHttpMessageConverer(MediaType.APPLICATION_JSON_UTF8));
	}

}
