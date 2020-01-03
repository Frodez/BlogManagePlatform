package frodez.service.cache.impl.user;

import frodez.config.cache.CacheProperties;
import frodez.constant.keys.cache.CacheKey;
import frodez.service.cache.facade.user.IdTokenCache;
import frodez.util.spring.ContextUtil;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * id-Token有持久化需要,故使用redis作为缓存
 * @author Frodez
 * @date 2019-12-31
 */
@Component("idTokenRedisCache")
public class IdTokenRedisCache implements IdTokenCache {

	private BoundHashOperations<String, String, String> idToken;

	private BoundHashOperations<String, String, String> tokenId;

	private Integer timeout;

	public IdTokenRedisCache(@Autowired CacheProperties properties, @Autowired StringRedisTemplate template) {
		idToken = template.boundHashOps(CacheKey.IdTokenCache.ID_TOKEN);
		tokenId = template.boundHashOps(CacheKey.IdTokenCache.TOKEN_ID);
		timeout = properties.getRedis().getTimeout();
		idToken.expire(timeout, TimeUnit.MINUTES);
		tokenId.expire(timeout, TimeUnit.MINUTES);
	}

	@Override
	public void clear() {
		StringRedisTemplate template = ContextUtil.bean(StringRedisTemplate.class);
		template.delete(CacheKey.IdTokenCache.ID_TOKEN);
		template.delete(CacheKey.IdTokenCache.TOKEN_ID);
		idToken = template.boundHashOps(CacheKey.IdTokenCache.ID_TOKEN);
		tokenId = template.boundHashOps(CacheKey.IdTokenCache.TOKEN_ID);
		idToken.expire(timeout, TimeUnit.MINUTES);
		tokenId.expire(timeout, TimeUnit.MINUTES);
	}

	@Override
	public boolean exist(Long id) {
		return idToken.hasKey(id.toString());
	}

	@Override
	public boolean exist(String token) {
		return tokenId.hasKey(token);
	}

	@Override
	public void save(Long id, String token) {
		String idString = id.toString();
		idToken.put(idString, token);
		tokenId.put(token, idString);
	}

	@Override
	public void remove(Long id) {
		String idString = id.toString();
		idToken.delete(idString);
	}

	@Override
	public void remove(String token) {
		tokenId.delete(token);
	}

	@Override
	public String getToken(Long id) {
		return idToken.get(id.toString());
	}

	@Override
	public Long getId(String token) {
		String idString = tokenId.get(token);
		return idString == null ? null : Long.valueOf(idString);
	}

	@Override
	public List<String> getTokens(List<Long> ids) {
		return idToken.multiGet(ids.stream().map((item) -> item.toString()).collect(Collectors.toList()));
	}

	@Override
	public void batchRemove(List<Long> ids, List<String> tokens) {
		idToken.delete(ids.stream().map((item) -> item.toString()).toArray());
		tokenId.delete(tokens.toArray());
	}

}
