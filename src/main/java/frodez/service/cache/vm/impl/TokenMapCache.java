package frodez.service.cache.vm.impl;

import frodez.dao.result.user.UserInfo;
import frodez.service.cache.vm.facade.TokenCache;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.springframework.stereotype.Component;

@Component
public class TokenMapCache implements TokenCache {

	/**
	 * 缓存 key:token, value:UserInfo
	 */
	private Map<String, UserInfo> cache = new ConcurrentHashMap<>();

	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public boolean existKey(String token) {
		return cache.containsKey(token);
	}

	@Override
	public boolean existValue(UserInfo userInfo) {
		return cache.containsValue(userInfo);
	}

	@Override
	public void save(String token, UserInfo userInfo) {
		cache.put(token, userInfo);
	}

	@Override
	public UserInfo get(String token) {
		return cache.get(token);
	}

	@Override
	public String getTokenByCondition(Predicate<UserInfo> predicate) {
		for (Entry<String, UserInfo> entry : cache.entrySet()) {
			if (predicate.test(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public List<String> getTokensByCondition(Predicate<UserInfo> predicate) {
		List<String> results = new ArrayList<>();
		for (Entry<String, UserInfo> entry : cache.entrySet()) {
			if (predicate.test(entry.getValue())) {
				results.add(entry.getKey());
			}
		}
		return results;
	}

	@Override
	public void remove(String token) {
		cache.remove(token);
	}

}
