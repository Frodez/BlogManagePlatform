package frodez.util.spring.properties;

import frodez.util.constant.setting.PropertyKey;
import frodez.util.spring.context.ContextUtil;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 访问控制参数配置
 * @author Frodez
 * @date 2018-11-14
 */
@Component
@DependsOn("contextUtil")
public class PropertyUtil {

	/**
	 * spring环境参数配置
	 */
	private static Environment env;

	@PostConstruct
	private void init() {
		env = ContextUtil.get(Environment.class);
	}

	/**
	 * 根据key获取配置
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static String get(String key) {
		return env.getProperty(key);
	}

	/**
	 * 根据key获取配置,获取失败返回默认值
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static String get(String key, String defaultValue) {
		return env.getProperty(key, defaultValue);
	}

	/**
	 * 获取当前激活的配置版本
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static List<String> activeProfiles() {
		return Arrays.asList(env.getActiveProfiles());
	}

	public static boolean isDev() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.DEV);
	}

	public static boolean isTest() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.TEST);
	}

	public static boolean isRelease() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.RELEASE);
	}

	public static boolean isProd() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.PROD);
	}

}
