package frodez.config.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 缓存配置
 * @author Frodez
 * @date 2019-03-01
 */
@Data
@Component
@PropertySource(value = { "classpath:settings/global/cache.properties" })
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

	/**
	 * 标准配置
	 */
	private StandardProperties standard = new StandardProperties();

	/**
	 * AutoGuavaChecker配置
	 */
	private AutoGuavaCheckerProperties autoGuavaChecker = new AutoGuavaCheckerProperties();

	/**
	 * ManualGuavaChecker配置
	 */
	private ManualGuavaCheckerProperties manualGuavaChecker = new ManualGuavaCheckerProperties();

	/**
	 * LimitUserGuavaChecker配置
	 */
	private LimitUserGuavaCheckerProperties limitUserGuavaChecker = new LimitUserGuavaCheckerProperties();

	/**
	 * redis配置
	 */
	private RedisProperties redis = new RedisProperties();

	@Data
	public static class StandardProperties {

		/**
		 * 超时时间,单位毫秒
		 */
		private Integer timeout = 60000;

	}

	@Data
	public static class AutoGuavaCheckerProperties {

		/**
		 * 超时时间,单位毫秒
		 */
		private Integer timeout = 60000;

	}

	@Data
	public static class ManualGuavaCheckerProperties {

		/**
		 * 超时时间,单位毫秒
		 */
		private Integer timeout = 60000;

	}

	@Data
	public static class LimitUserGuavaCheckerProperties {

		/**
		 * 超时时间,单位毫秒
		 */
		private Integer timeout = 60000;

	}

	@Data
	public static class RedisProperties {

		/**
		 * 超时时间,单位分钟
		 */
		private Integer timeout = 7 * 1440;

	}

}
