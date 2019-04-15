package frodez.config.security.settings;

import java.util.ArrayList;
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
	 * 跨域参数
	 */
	private Cors cors = new Cors();

	/**
	 * jwt参数
	 */
	private Jwt jwt = new Jwt();

	/**
	 * 验证配置
	 */
	private Auth auth = new Auth();

	/**
	 * 跨域参数配置
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Data
	public static class Cors {

		private List<String> allowedOrigins = new ArrayList<>();

		private List<String> allowedMethods = new ArrayList<>();

		private List<String> allowedHeaders = new ArrayList<>();

	}

	/**
	 * jwt参数配置
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Data
	public static class Jwt {

		/**
		 * HTTP请求header名称
		 */
		private String header = "";

		/**
		 * jwt密钥
		 */
		private String secret = "";

		/**
		 * jwt过期时间(小于等于0为不设置过期时间)
		 */
		private Long expiration = 0L;

		/**
		 * jwt签发者
		 */
		private String issuer = "";

		/**
		 * HTTP请求header前缀
		 */
		private String tokenPrefix = "";

		/**
		 * jwt权限保留字
		 */
		private String authorityClaim = "";

	}

	/**
	 * 跨域参数配置
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Data
	public static class Auth {

		/**
		 * 免验证路径,基于正则表达式匹配
		 */
		private List<String> permitAllPath = new ArrayList<>();

		/**
		 * 无权限角色
		 */
		private String deniedRole = "";

	}

}
