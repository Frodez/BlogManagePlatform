package frodez.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * swagger参数配置
 * @author Frodez
 * @date 2019-01-06
 */
@Data
@Component
@Profile({ "dev", "test" })
@PropertySource(value = { "classpath:settings/global/swagger.properties" })
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

	/**
	 * 接口位置
	 */
	private String basePackage = "";

	/**
	 * 标题
	 */
	private String title = "";

	/**
	 * 描述
	 */
	private String description = "";

	/**
	 * 作者
	 */
	private String author = "";

	/**
	 * 文档链接
	 */
	private String docUrl = "";

	/**
	 * 邮箱地址
	 */
	private String email = "";

}
