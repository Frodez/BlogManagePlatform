package frodez.config.security.settings;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import frodez.constant.setting.DefTime;
import frodez.constant.setting.PropertyKey;
import frodez.util.spring.properties.PropertyUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
@PropertySource("classpath:settings/security.properties")
@ConfigurationProperties
public class SecurityProperties {

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
	 * 垃圾收集间隔(毫秒)
	 */
	private static final int GC_INTERVAL = 3600000;

	/**
	 * 最大缓存个数
	 */
	private static final int CACHE_SIZE = 65536;

	/**
	 * url匹配缓存
	 */
	private static final Cache<String, Boolean> URL_CACHE = CacheBuilder.newBuilder().maximumSize(CACHE_SIZE)
		.expireAfterAccess(GC_INTERVAL, DefTime.UNIT).build();

	/**
	 * 判断url是否需要验证<br>
	 * <strong>建议url不要带入任何path类型参数,以提高性能!</strong>
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public boolean needVerify(String url) {
		Boolean result = URL_CACHE.getIfPresent(url);
		if (result != null) {
			return result;
		}
		for (String path : auth.getPermitAllPath()) {
			if (matcher.match(PropertyUtil.get(PropertyKey.Web.BASE_PATH) + path, url)) {
				URL_CACHE.put(url, false);
				return false;
			}
		}
		URL_CACHE.put(url, true);
		return true;
	}

	/**
	 * 获取request中的token,如果为空或者前缀不符合设置,均返回空.
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public String getRealToken(HttpServletRequest request) {
		String token = request.getHeader(jwt.header);
		if (token == null || !token.startsWith(jwt.tokenPrefix)) {
			return null;
		}
		return token.substring(jwt.tokenPrefix.length());
	}

	/**
	 * 获取request中的token.
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public String getFullToken(HttpServletRequest request) {
		return request.getHeader(jwt.header);
	}

}
