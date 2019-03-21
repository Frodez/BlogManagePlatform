package frodez.config.task;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 * @author Frodez
 * @date 2019-03-20
 */
@Data
@Component
@PropertySource(value = { "classpath:settings/${spring.profiles.active}/task.properties" })
@ConfigurationProperties(prefix = "task")
public class TaskProperties {

	/**
	 * 前缀
	 */
	private String prefix = "";

	/**
	 * 最大任务数量
	 */
	private Integer maxSize = 127;

}
