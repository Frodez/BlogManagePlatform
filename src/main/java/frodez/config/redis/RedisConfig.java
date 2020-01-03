package frodez.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 * @author Frodez
 * @date 2018-12-21
 */
@Configuration
public class RedisConfig {

	@Bean
	public StringRedisTemplate StringRedisTemplate(RedisConnectionFactory connectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setStringSerializer(StringRedisSerializer.UTF_8);
		template.setEnableTransactionSupport(true);
		template.afterPropertiesSet();
		return template;
	}

}
