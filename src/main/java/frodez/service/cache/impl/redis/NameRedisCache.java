package frodez.service.cache.impl.redis;

import frodez.constant.keys.cache.CacheKey;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.facade.NameCache;
import frodez.util.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component("nameRedisCache")
public class NameRedisCache implements NameCache {

	private BoundHashOperations<String, String, String> ops;

	public NameRedisCache(@Autowired StringRedisTemplate redis) {
		ops = redis.boundHashOps(CacheKey.NameCache.KEY);
	}

	@Override
	public long size() {
		return ops.size();
	}

	@Override
	public boolean existKey(String name) {
		return ops.hasKey(name);
	}

	@Override
	public boolean existValue(UserInfo userInfo) {
		return ops.get(userInfo.getName()) != null;
	}

	@Override
	public void save(String name, UserInfo userInfo) {
		ops.put(name, JSONUtil.string(userInfo));
	}

	@Override
	public UserInfo get(String name) {
		String json = ops.get(name);
		return json == null ? null : JSONUtil.as(json, UserInfo.class);
	}

	@Override
	public void remove(String name) {
		ops.delete(name);
	}

}
