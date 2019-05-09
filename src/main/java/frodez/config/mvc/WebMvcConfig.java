package frodez.config.mvc;

import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
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
		//清除掉原本的AbstractJackson2HttpMessageConverter和StringHttpMessageConverter。
		converters.removeIf((iter) -> {
			return iter instanceof AbstractJackson2HttpMessageConverter || iter instanceof StringHttpMessageConverter;
		});
		converters.add(0, new JsonConverer(MediaType.APPLICATION_JSON_UTF8));
		converters.add(1, new StringEscapeConverter());
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		//对字符串进行转义
		registry.addConverter(new Converter<String, String>() {

			private final Escaper escaper = HtmlEscapers.htmlEscaper();

			@Override
			public String convert(String source) {
				return escaper.escape(source);
			}
		});

	}

}
