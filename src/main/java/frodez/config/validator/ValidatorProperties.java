package frodez.config.validator;

import java.util.Arrays;
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
@PropertySource(value = { "classpath:settings/global/validator/validator.properties" })
@ConfigurationProperties(prefix = "validator")
public class ValidatorProperties {

	/**
	 * 开启代码检查的环境名称
	 */
	private List<String> enviroments = Arrays.asList("dev", "test");

	/**
	 * 检查规则的来源,ant风格匹配
	 */
	private String rulePath = "frodez.config.code.rule.*";

	/**
	 * 需要检查的代码,ant风格匹配
	 */
	private List<String> modelPath = Arrays.asList("frodez.*.*");

	/**
	 * 消息插值配置路径(classpath下,不需增加classpath前缀)
	 */
	private String messageConfigPath;

	/**
	 * 是否开启快速失败模式
	 */
	private Boolean failFast = true;

}
