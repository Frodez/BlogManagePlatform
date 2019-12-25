package frodez.service.cache.impl.redis;

import frodez.constant.keys.cache.CacheKey;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.facade.UserIdCache;
import frodez.util.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component("userIdRedisCache")
public class UserIdRedisCache implements UserIdCache {

	private BoundHashOperations<String, String, String> ops;

	public UserIdRedisCache(@Autowired StringRedisTemplate redis) {
		ops = redis.boundHashOps(CacheKey.UserIdCache.KEY);
	}

	@Override
	public long size() {
		return ops.size();
	}

	@Override
	public boolean existKey(Long userId) {
		return ops.hasKey(userId.toString());
	}

	@Override
	public boolean existValue(UserInfo userInfo) {
		return ops.get(userInfo.getId().toString()) != null;
	}

	@Override
	public void save(Long userId, UserInfo userInfo) {
		ops.put(userId.toString(), JSONUtil.string(userInfo));
	}

	@Override
	public UserInfo get(Long userId) {
		String json = ops.get(userId);
		return json == null ? null : JSONUtil.as(json, UserInfo.class);
	}

	@Override
	public void remove(Long userId) {
		ops.delete(userId.toString());
	}

}
