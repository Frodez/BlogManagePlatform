package frodez.config.security.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 访问控制参数配置
 * @author Frodez
 * @date 2018-11-14
 */
@Data
@Component
@PropertySource(value = { "classpath:settings/${spring.profiles.active}/security.properties" })
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

	/**
	 * 验证配置
	 */
	private Auth auth = new Auth();

	/**
	 * 跨域参数
	 */
	private Cors cors = new Cors();

	/**
	 * jwt参数
	 */
	private Jwt jwt = new Jwt();

	/**
	 * 验证配置
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Data
	public static class Auth {

		/**
		 * 无权限角色
		 */
		private String deniedRole = "";

		/**
		 * 免验证路径,基于正则表达式匹配
		 */
		private List<String> permitAllPath = new ArrayList<>();

	}

	/**
	 * 跨域参数配置
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Data
	public static class Cors {

		/**
		 * 允许http headers
		 */
		private List<String> allowedHeaders = Arrays.asList("Origin", "X-Requested-With", "Content-Type", "Accept",
			"Accept-Encoding", "Accept-Language", "Host", "Referer", "Connection", "User-Agent", "Authorization");

		/**
		 * 允许http methods
		 */
		private List<String> allowedMethods = Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS");

		/**
		 * 允许http origins
		 */
		private List<String> allowedOrigins = new ArrayList<>();

	}

	/**
	 * jwt参数配置
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Data
	public static class Jwt {

		/**
		 * jwt权限保留字
		 */
		private String authorityClaim = "";

		/**
		 * jwt过期时间(小于等于0为不设置过期时间)
		 */
		private Long expiration = 0L;

		/**
		 * HTTP请求header名称
		 */
		private String header = "Authorization";

		/**
		 * jwt签发者
		 */
		private String issuer = "";

		/**
		 * jwt密钥
		 */
		private String secret = "";

		/**
		 * HTTP请求header前缀
		 */
		private String tokenPrefix = "";

	}

}
