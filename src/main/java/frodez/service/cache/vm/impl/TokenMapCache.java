package frodez.service.cache.vm.impl;

import frodez.dao.result.user.UserInfo;
import frodez.service.cache.vm.facade.TokenCache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TokenMapCache implements TokenCache {

	/**
	 * 缓存 key:token, value:UserInfo
	 */
	private Map<String, UserInfo> cache = new ConcurrentHashMap<>();

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
		if (!cache.containsKey(token)) {
			throw new RuntimeException("缓存中无此token!");
		}
		return cache.get(token);
	}

	@Override
	public void remove(String token) {
		if (!cache.containsKey(token)) {
			throw new RuntimeException("缓存中无此token!");
		}
		cache.remove(token);
	}

}
