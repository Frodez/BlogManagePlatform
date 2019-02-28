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
@PropertySource("classpath:settings/cache.properties")
@ConfigurationProperties
public class CacheProperties {

	private AutoGuavaChecker autoGuavaChecker = new AutoGuavaChecker();

	private ManualGuavaChecker manualGuavaChecker = new ManualGuavaChecker();

	private URLMatcher urlMatcher = new URLMatcher();

	@Data
	public static class AutoGuavaChecker {

		private Integer timeout = 60000;

	}

	@Data
	public static class ManualGuavaChecker {

		private Integer timeout = 60000;

	}

	@Data
	public static class URLMatcher {

		private Integer timeout = 3600000;

		private Integer maxSize = 65536;

	}

}
