package frodez.service.cache.redis;

import frodez.constant.setting.DefTime;
import frodez.util.common.EmptyUtil;
import frodez.util.json.JSONUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis服务
 * @author Frodez
 * @date 2018-12-21
 */
@Component
public class RedisService {

	/**
	 * RedisTemplate
	 */
	@Autowired
	private RedisTemplate<Object, Object> template;

	/**
	 * 根据key获得value
	 * @param key
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public Object get(Object key) {
		return template.opsForValue().get(key);
	}

	/**
	 * 根据key获得value(String类型),若无返回值则返回null
	 * @param key
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public String getString(Object key) {
		Object value = template.opsForValue().get(key);
		if (value == null) {
			return null;
		}
		if (value.getClass() == String.class) {
			return (String) value;
		}
		return JSONUtil.toJSONString(value);
	}

	/**
	 * 判断是否存在对应key,返回true则存在,false则不存在
	 * @param key
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public boolean exists(Object key) {
		return template.hasKey(key);
	}

	/**
	 * 为key设置value
	 * @param key
	 * @param value
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public void set(Object key, Object value) {
		template.opsForValue().set(key, value);
	}

	/**
	 * 为key设置value,且有过期时间
	 * @param key
	 * @param value
	 * @param timeout 过期时间(单位毫秒)
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public void set(Object key, Object value, long timeout) {
		template.opsForValue().set(key, value, timeout, DefTime.UNIT);
	}

	/**
	 * 为key设置value,且有过期时间
	 * @param key
	 * @param value
	 * @param timeout 过期时间
	 * @param TimeUnit 过期时间单位
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public void set(Object key, Object value, long timeout, TimeUnit timeUnit) {
		template.opsForValue().set(key, value, timeout, timeUnit);
	}

	/**
	 * 删除key
	 * @param key
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public boolean delete(Object key) {
		return template.delete(key);
	}

	/**
	 * 删除适配的key
	 * @param pattern 适配器
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public long deletePattern(Object pattern) {
		Set<Object> keys = template.keys(pattern);
		if (EmptyUtil.no(keys)) {
			return template.delete(keys);
		}
		return 0;
	}

	/**
	 * 批量删除key
	 * @param keys
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public void delete(Object... keys) {
		if (keys != null && keys.length != 0) {
			template.delete(Arrays.asList(keys));
		}
	}

	/**
	 * 批量删除key
	 * @param keys
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public void delete(List<Object> keys) {
		if (EmptyUtil.no(keys)) {
			template.delete(keys);
		}
	}

	/**
	 * 为hashMap赋值
	 * @param key hashMap所对应的key
	 * @param hashKey hashMap中的key
	 * @param value
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public void hmset(Object key, Object hashKey, Object value) {
		template.opsForHash().put(key, hashKey, value);
	}

	/**
	 * 为hashMap赋值
	 * @param key hashMap所对应的key
	 * @param hashMap
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public void hmset(Object key, Map<Object, Object> hashMap) {
		template.opsForHash().putAll(key, hashMap);
	}

	/**
	 * 根据key获取hashMap
	 * @param key
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public Map<Object, Object> hmget(Object key) {
		return template.opsForHash().entries(key);
	}

	/**
	 * 根据key获得hashMap(String类型),若无返回值则返回null
	 * @param key
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public String hmgetString(Object key) {
		Map<Object, Object> map = template.opsForHash().entries(key);
		if (EmptyUtil.no(map)) {
			return null;
		}
		return JSONUtil.toJSONString(map);
	}

	/**
	 * 根据key和hashKey获取对应hashMap中的值
	 * @param key
	 * @param hashKey
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public Object hmget(Object key, Object hashKey) {
		return template.opsForHash().get(key, hashKey);
	}

	/**
	 * 根据key获得hashMap的value(String类型),若无返回值则返回null
	 * @param key
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public String hmgetString(Object key, Object hashKey) {
		Object value = template.opsForHash().get(key, hashKey);
		if (value == null) {
			return null;
		}
		if (value.getClass() == String.class) {
			return (String) value;
		}
		return JSONUtil.toJSONString(value);
	}

	/**
	 * 判断是否存在对应hashMap的key,返回true则存在,false则不存在
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public boolean hmexists(Object key) {
		return EmptyUtil.no(template.opsForHash().values(key));
	}

}
