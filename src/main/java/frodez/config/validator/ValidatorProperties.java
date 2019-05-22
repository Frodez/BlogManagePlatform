package frodez.config.validator;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * hibernate-validator配置
 * @author Frodez
 * @date 2019-05-22
 */
@Data
@Component
@PropertySource(value = { "classpath:settings/global/validator.properties" })
@ConfigurationProperties(prefix = "validator")
public class ValidatorProperties {

	/**
	 * 是否开启代码检查
	 */
	private Boolean codeReview = true;

	/**
	 * 需要检查的代码,ant风格匹配
	 */
	private List<String> modelPath;

	/**
	 * 消息插值配置路径(classpath下,不需增加classpath前缀)
	 */
	private String messageConfigPath;

}
