package frodez.service.cache.impl.redis;

import frodez.constant.keys.cache.CacheKey;
import frodez.constant.settings.DefStr;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.facade.TokenCache;
import frodez.util.common.StrUtil;
import frodez.util.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component("tokenRedisCache")
public class TokenRedisCache implements TokenCache {

	private BoundHashOperations<String, String, String> tokenCache;

	private BoundHashOperations<String, String, String> idCache;

	public TokenRedisCache(@Autowired StringRedisTemplate redis) {
		tokenCache = redis.boundHashOps(CacheKey.TokenCache.KEY);
		idCache = redis.boundHashOps(CacheKey.TokenCache.ID);
	}

	private String generateIdKey(Long id) {
		return StrUtil.concat(CacheKey.TokenCache.ID, DefStr.SEPERATOR, id.toString());
	}

	@Override
	public long size() {
		return tokenCache.size();
	}

	@Override
	public boolean existToken(String token) {
		return tokenCache.hasKey(token);
	}

	@Override
	public boolean existId(Long id) {
		return idCache.hasKey(id.toString());
	}

	@Override
	public void save(String token, UserInfo userInfo) {
		tokenCache.put(token, JSONUtil.string(userInfo));
		idCache.put(userInfo.getId().toString(), token);
	}

	@Override
	public UserInfo get(String token) {
		String json = tokenCache.get(token);
		return json == null ? null : JSONUtil.as(json, UserInfo.class);
	}

	@Override
	public String getTokenById(Long id) {
		return idCache.get(generateIdKey(id));
	}

	@Override
	public void remove(String token) {
		String json = tokenCache.get(token);
		if (json == null) {
			return;
		}
		tokenCache.delete(token);
		idCache.delete(JSONUtil.as(json, UserInfo.class).getId().toString());
	}

}
