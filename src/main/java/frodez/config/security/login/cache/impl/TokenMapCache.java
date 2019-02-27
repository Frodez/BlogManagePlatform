package frodez.config.security.login.cache.impl;

import frodez.config.security.login.cache.facade.TokenCache;
import frodez.service.user.result.UserInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TokenMapCache implements TokenCache {

	/**
	 * 缓存 key:token, value:UserInfo
	 */
	private static final Map<String, UserInfo> CACHE = new ConcurrentHashMap<>();

	@Override
	public boolean exist(String token) {
		return CACHE.containsKey(token);
	}

	@Override
	public boolean exist(UserInfo userInfo) {
		return CACHE.containsValue(userInfo);
	}

	@Override
	public void save(String token, UserInfo userInfo) {
		CACHE.put(token, userInfo);
	}

	@Override
	public UserInfo get(String token) {
		if (!CACHE.containsKey(token)) {
			throw new RuntimeException("缓存中无此token!");
		}
		return CACHE.get(token);
	}

	@Override
	public void remove(String token) {
		if (!CACHE.containsKey(token)) {
			throw new RuntimeException("缓存中无此token!");
		}
		CACHE.remove(token);
	}

}
