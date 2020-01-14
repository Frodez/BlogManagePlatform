package frodez.util.spring;

import frodez.constant.keys.spring.PropertyKey;
import frodez.util.common.EmptyUtil;
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

	private static List<String> activeEnvs;

	@PostConstruct
	private void init() {
		env = ContextUtil.bean(Environment.class);
		activeEnvs = List.of(env.getActiveProfiles());
		Assert.notNull(env, "env must not be null");
		Assert.notNull(activeEnvs, "activeEnvs must not be null");
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
		return activeEnvs;
	}

	/**
	 * 判断是否与激活的配置匹配
	 * @author Frodez
	 * @date 2019-06-05
	 */
	public static boolean matchEnvs(List<String> enviroments) {
		if (EmptyUtil.yes(enviroments)) {
			throw new IllegalArgumentException("需要匹配的环境不能为空!");
		}
		return enviroments.stream().anyMatch((env) -> {
			return activeEnvs.contains(env);
		});
	}

	/**
	 * 判断是否与激活的配置匹配
	 * @author Frodez
	 * @date 2019-06-05
	 */
	public static boolean matchEnvs(String... enviroments) {
		if (EmptyUtil.yes(enviroments)) {
			throw new IllegalArgumentException("需要匹配的环境不能为空!");
		}
		return Arrays.stream(enviroments).anyMatch((env) -> {
			return activeEnvs.contains(env);
		});
	}

	/**
	 * 当前环境是否为dev环境<br>
	 * <strong>本实现不考虑同时启用多个环境的情况。</strong>
	 * @see frodez.constant.keys.spring.PropertyKey.Enviroment#DEV
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static boolean isDev() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.DEV);
	}

	/**
	 * 当前环境是否为test环境<br>
	 * <strong>本实现不考虑同时启用多个环境的情况。</strong>
	 * @see frodez.constant.keys.spring.PropertyKey.Enviroment#TEST
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static boolean isTest() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.TEST);
	}

	/**
	 * 当前环境是否为release环境<br>
	 * <strong>本实现不考虑同时启用多个环境的情况。</strong>
	 * @see frodez.constant.keys.spring.PropertyKey.Enviroment#RELEASE
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static boolean isRelease() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.RELEASE);
	}

	/**
	 * 当前环境是否为prod环境<br>
	 * <strong>本实现不考虑同时启用多个环境的情况。</strong>
	 * @see frodez.constant.keys.spring.PropertyKey.Enviroment#PROD
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static boolean isProd() {
		return env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.PROD);
	}

}
