package frodez.config.security.login.cache.impl;

import frodez.config.security.login.cache.facade.NameCache;
import frodez.service.user.result.UserInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class NameMapCache implements NameCache {

	/**
	 * 缓存 key:name, value:UserInfo
	 */
	private static final Map<String, UserInfo> CACHE = new ConcurrentHashMap<>();

	@Override
	public boolean exist(String name) {
		return CACHE.containsKey(name);
	}

	@Override
	public boolean exist(UserInfo userInfo) {
		return CACHE.containsValue(userInfo);
	}

	@Override
	public void save(String name, UserInfo userInfo) {
		CACHE.put(name, userInfo);
	}

	@Override
	public UserInfo get(String name) {
		return CACHE.get(name);
	}

	@Override
	public void remove(String name) {
		CACHE.remove(name);
	}

}
