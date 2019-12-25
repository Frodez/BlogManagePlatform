package frodez.service.cache.impl.vm;

import frodez.dao.result.user.UserInfo;
import frodez.service.cache.facade.TokenCache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component("tokenMapCache")
public class TokenMapCache implements TokenCache {

	/**
	 * 缓存 key:token, value:UserInfo
	 */
	private Map<String, UserInfo> tokenCache = new ConcurrentHashMap<>();

	/**
	 * 缓存 key:id value:token
	 */
	private Map<Long, String> idCache = new ConcurrentHashMap<>();

	@Override
	public long size() {
		return tokenCache.size();
	}

	@Override
	public boolean existToken(String token) {
		return tokenCache.containsKey(token);
	}

	@Override
	public boolean existId(Long id) {
		return idCache.containsKey(id);
	}

	@Override
	public void save(String token, UserInfo userInfo) {
		tokenCache.put(token, userInfo);
		idCache.put(userInfo.getId(), token);
	}

	@Override
	public UserInfo get(String token) {
		return tokenCache.get(token);
	}

	@Override
	public String getTokenById(Long id) {
		return idCache.get(id);
	}

	@Override
	public void remove(String token) {
		UserInfo info = tokenCache.get(token);
		if (info == null) {
			return;
		}
		tokenCache.remove(token);
		idCache.remove(info.getId());
	}

}
