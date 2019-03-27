package frodez.config.redis;

import frodez.util.json.JSONUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 * @author Frodez
 * @date 2018-12-21
 */
@Configuration
@DependsOn("jsonUtil")
public class RedisConfig {

	/**
	 * 获取RedisTemplate实例
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值(如不设置,则默认使用JDK的序列化方式)
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
		serializer.setObjectMapper(JSONUtil.mapper());
		template.setKeySerializer(serializer);
		template.setValueSerializer(serializer);
		template.setHashKeySerializer(serializer);
		template.setHashValueSerializer(serializer);
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setStringSerializer(StringRedisSerializer.UTF_8);
		template.afterPropertiesSet();
		return template;
	}

}
