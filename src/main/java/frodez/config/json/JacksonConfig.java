package frodez.config.json;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import frodez.util.json.JSONUtil;

/***
 * jackson配置类,用于将JSONUtil中的objectMapper织入spring.<br>
 * 这样可以使得系统中只存在一个objectMapper,同时objectMapper是static final对象,不必更改.<br>
 * 未来视情况(比如objectMapper可能需要改变)完全可以让objectMapper重新由spring自动注入,而非自己配置.<br>
 * @author Frodez
 * @date 2019-01-08
 */
@Configuration
public class JacksonConfig {

	@Bean
	@Primary
	@ConditionalOnMissingBean(ObjectMapper.class)
	public ObjectMapper objectMapper() {
		return JSONUtil.getInstance();
	}

}
