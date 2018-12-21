package info.frodez.util.redis;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import info.frodez.util.json.JSONUtil;

@Component
public class RedisService {

	@Autowired
	private RedisTemplate<Object, Object> template;

	public Object get(Object key) {
		return template.opsForValue().get(key);
	}

	public String getString(Object key) {
		Object value = template.opsForValue().get(key);
		if(value == null) {
			return null;
		}
		return JSONUtil.toJSONString(value);
	}

	public boolean exists(Object key) {
		return template.hasKey(key);
	}

	public void set(Object key, Object value) {
		template.opsForValue().set(key, value);
	}

	public void set(Object key, Object value, long timeout) {
		template.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
	}

	public void set(Object key, Object value, long timeout, TimeUnit timeUnit) {
		template.opsForValue().set(key, value, timeout, timeUnit);
	}

	public boolean delete(Object key) {
		return template.delete(key);
	}

	public long deletePattern(Object pattern) {
		Set<Object> keys = template.keys(pattern);
		if(CollectionUtils.isNotEmpty(keys)) {
			return template.delete(keys);
		}
		return 0;
	}

	public void delete(Object... keys) {
		if(keys != null && keys.length != 0) {
			template.delete(Arrays.asList(keys));
		}
	}

	public void delete(List<Object> keys) {
		if(CollectionUtils.isNotEmpty(keys)) {
			template.delete(keys);
		}
	}

	public void hmset(Object key, Object hashKey, Object value) {
		template.opsForHash().put(key, hashKey, value);
	}

	public void hmset(Object key, Map<Object, Object> value) {
		template.opsForHash().putAll(key, value);
	}

	public Map<Object, Object> hmget(Object key) {
		return template.opsForHash().entries(key);
	}

	public String hmgetString(Object key) {
		Map<Object, Object> map = template.opsForHash().entries(key);
		if(MapUtils.isEmpty(map)) {
			return null;
		}
		return JSONUtil.toJSONString(map);
	}

	public Object hmget(Object key, Object hashKey) {
		return template.opsForHash().get(key, hashKey);
	}

	public String hmgetString(Object key, Object hashKey) {
		Object value = template.opsForHash().get(key, hashKey);
		if(value == null) {
			return null;
		}
		return JSONUtil.toJSONString(value);
	}

	public boolean hmexists(Object key) {
		return template.opsForHash().values(key).size() != 0;
	}

}
