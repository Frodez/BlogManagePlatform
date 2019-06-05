package frodez.config.mvc.async;

import java.util.concurrent.ThreadPoolExecutor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步配置
 * @author Frodez
 * @date 2019-04-15
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig {

	@Autowired
	@Getter
	private AsyncProperties properties;

	public AsyncTaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		int corePoolSize = Math.round(availableProcessors * properties.getCoreThreadTimes());
		int maxPoolSize = Math.round(availableProcessors * properties.getMaxThreadTimes());
		int queueCapacity = Math.round(maxPoolSize * properties.getQueueFactors());
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
		executor.setThreadNamePrefix(properties.getThreadNamePrefix());
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		log.info("async executor is already now!");
		log.info(
			"async config:corePoolSize-{}, maxPoolSize-{}, queueCapacity-{}, keepAliveSeconds-{}, threadNamePrefix-{}",
			corePoolSize, maxPoolSize, queueCapacity, properties.getKeepAliveSeconds(), properties
				.getThreadNamePrefix());
		return executor;
	}

}
