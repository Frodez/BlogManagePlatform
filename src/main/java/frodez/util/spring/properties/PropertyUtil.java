package frodez.util.spring.properties;

import frodez.constant.setting.PropertyKey;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 访问控制参数配置
 * @author Frodez
 * @date 2018-11-14
 */
@Getter
@Component
public class PropertyUtil {

	/**
	 * spring环境参数配置
	 */
	@Autowired
	private Environment env;

	private static PropertyUtil propertyUtil;

	@PostConstruct
	private void init() {
		propertyUtil = this;
	}

	/**
	 * 根据key获取配置
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static String get(String key) {
		return propertyUtil.env.getProperty(key);
	}

	/**
	 * 根据key获取配置,获取失败返回默认值
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static String get(String key, String defaultValue) {
		return propertyUtil.env.getProperty(key, defaultValue);
	}

	/**
	 * 获取当前激活的配置版本
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static List<String> getActiveProfiles() {
		return Arrays.asList(propertyUtil.env.getActiveProfiles());
	}

	public static boolean isDev() {
		return propertyUtil.env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.DEV);
	}

	public static boolean isTest() {
		return propertyUtil.env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.TEST);
	}

	public static boolean isRelease() {
		return propertyUtil.env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.RELEASE);
	}

	public static boolean isProd() {
		return propertyUtil.env.getActiveProfiles()[0].equals(PropertyKey.Enviroment.PROD);
	}

}
