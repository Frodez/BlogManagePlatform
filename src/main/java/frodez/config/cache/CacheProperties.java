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
@PropertySource(value = { "classpath:settings/cache.properties" })
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

	private StandardProperties standard = new StandardProperties();

	private AutoGuavaCheckerProperties autoGuavaChecker = new AutoGuavaCheckerProperties();

	private ManualGuavaCheckerProperties manualGuavaChecker = new ManualGuavaCheckerProperties();

	private LimitUserGuavaCheckerProperties limitUserGuavaChecker = new LimitUserGuavaCheckerProperties();

	private URLMatcherProperties urlMatcher = new URLMatcherProperties();

	@Data
	public static class StandardProperties {

		private Integer timeout = 60000;

	}

	@Data
	public static class AutoGuavaCheckerProperties {

		private Integer timeout = 60000;

	}

	@Data
	public static class ManualGuavaCheckerProperties {

		private Integer timeout = 60000;

	}

	@Data
	public static class LimitUserGuavaCheckerProperties {

		private Integer timeout = 60000;

	}

	@Data
	public static class URLMatcherProperties {

		private Integer timeout = 3600000;

		private Integer maxSize = 65536;

	}

}
