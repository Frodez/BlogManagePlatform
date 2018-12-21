package info.frodez.config.security.settings;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 访问控制参数配置
 * @author Frodez
 * @date 2018-11-14
 */
@Data
@Component
@PropertySource("classpath:security.properties")
@ConfigurationProperties
public class SecurityProperties {

	private Cors cors = new Cors();

	private Jwt jwt = new Jwt();

	@Data
	public static class Cors {

		private List<String> allowedOrigins = new ArrayList<>();

		private List<String> allowedMethods = new ArrayList<>();

		private List<String> allowedHeaders = new ArrayList<>();

	}

	@Data
	public static class Jwt {

		private String header;

		private String secret;

		private Long expiration;

		private String issuer;

		private String authenticationPath;

	}

}
