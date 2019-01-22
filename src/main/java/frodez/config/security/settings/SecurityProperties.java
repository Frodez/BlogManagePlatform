package frodez.config.security.settings;

import frodez.util.spring.properties.PropertiesUtil;
import frodez.util.spring.properties.PropertyKey;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

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

	/**
	 * spring环境参数配置
	 */
	@Autowired
	private PropertiesUtil springProperties;

	/**
	 * spring路径匹配器
	 */
	@Autowired
	private PathMatcher matcher;

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
		 * jwt过期时间
		 */
		private Long expiration = (long) -1;

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

	/**
	 * 判断url是否需要验证
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public boolean needVerify(String url) {
		for (String path : auth.getPermitAllPath()) {
			if (matcher.match(springProperties.get(PropertyKey.Web.BASE_PATH) + path, url)) {
				return false;
			}
		}
		return true;
	}

}
