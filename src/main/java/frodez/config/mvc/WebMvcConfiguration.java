package frodez.config.mvc;

import java.util.Iterator;
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
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() instanceof AbstractJackson2HttpMessageConverter) {
				iterator.remove();
			}
		}
		converters.add(0, new JsonHttpMessageConverer(MediaType.APPLICATION_JSON_UTF8));
	}

}
