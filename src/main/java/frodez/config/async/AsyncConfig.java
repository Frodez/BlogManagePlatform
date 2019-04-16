package frodez.config.async;

import frodez.util.spring.ContextUtil;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

	@Bean
	public Executor getAsyncExecutor() {
		AsyncProperties properties = ContextUtil.get(AsyncProperties.class);
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
		log.info("async executor is running now!");
		return executor;
	}

}
