package info.frodez.config.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * swagger参数配置
 * @author Frodez
 * @date 2019-01-06
 */
@Getter
@Component
@PropertySource("classpath:swagger.properties")
@ConfigurationProperties
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
