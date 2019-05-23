package frodez.config.mvc;

import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;
import frodez.config.mvc.async.AsyncConfig;
import frodez.config.mvc.converter.JsonConverter;
import frodez.config.mvc.converter.ResultConverter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutDeferredResultProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * springMVC配置
 * @author Frodez
 * @date 2019-03-14
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private AsyncConfig asyncConfig;

	/**
	 * 配置消息转换器
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//清除掉原本的AbstractJackson2HttpMessageConverter。
		converters.removeIf((iter) -> {
			return iter instanceof AbstractJackson2HttpMessageConverter;
		});
		converters.add(0, new ResultConverter());
		converters.add(1, new JsonConverter());
	}

	/**
	 * 配置格式化器
	 * @author Frodez
	 * @date 2019-05-10
	 */
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

	/**
	 * 配置异步
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(asyncConfig.getAsyncExecutor());
		configurer.setDefaultTimeout(asyncConfig.getProperties().getTimeout());
		configurer.registerCallableInterceptors(new TimeoutCallableProcessingInterceptor());
		configurer.registerDeferredResultInterceptors(new TimeoutDeferredResultProcessingInterceptor());
	}

	/**
	 * 配置默认媒体类型
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
	}

}
