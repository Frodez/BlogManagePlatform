package frodez.config.async;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 异步任务配置
 * @author Frodez
 * @date 2019-04-15
 */
@Data
@Component
@PropertySource(value = { "classpath:settings/${spring.profiles.active}/async.properties" })
@ConfigurationProperties(prefix = "async")
public class AsyncProperties {

	/**
	 * 核心线程基数,实际数量等于系统环境可用核心数乘以该基数并四舍五入。
	 */
	private float coreThreadTimes = 0.5F;

	/**
	 * 最大线程基数,实际数量等于系统环境可用核心数乘以该基数并四舍五入。
	 */
	private float maxThreadTimes = 1.0F;

	/**
	 * 队列规模因子,队列最大长度等于计算出的最大线程数乘以规模因子并四舍五入。
	 */
	private float queueFactors = 16.0F;

	/**
	 * 线程最长活跃时间,单位为秒
	 */
	private int keepAliveSeconds = 60;

	/**
	 * 线程名前缀
	 */
	private String threadNamePrefix = "async";

}
