package frodez.util.spring;

import frodez.constant.settings.PropertyKey;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
		Assert.notNull(env, "env must not be null");
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

	/**
	 * 当前环境是否为dev环境<br>
	 * <strong>本实现不考虑同时启用多个环境的情况。</strong>
	 * @see frodez.constant.settings.PropertyKey.Enviroment#DEV
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static boolean isDev() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.DEV);
	}

	/**
	 * 当前环境是否为test环境<br>
	 * <strong>本实现不考虑同时启用多个环境的情况。</strong>
	 * @see frodez.constant.settings.PropertyKey.Enviroment#TEST
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static boolean isTest() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.TEST);
	}

	/**
	 * 当前环境是否为release环境<br>
	 * <strong>本实现不考虑同时启用多个环境的情况。</strong>
	 * @see frodez.constant.settings.PropertyKey.Enviroment#RELEASE
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static boolean isRelease() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.RELEASE);
	}

	/**
	 * 当前环境是否为prod环境<br>
	 * <strong>本实现不考虑同时启用多个环境的情况。</strong>
	 * @see frodez.constant.settings.PropertyKey.Enviroment#PROD
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static boolean isProd() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.PROD);
	}

}
