package frodez.cache.vm.impl;

import frodez.cache.vm.facade.NameCache;
import frodez.service.user.result.UserInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class NameMapCache implements NameCache {

	/**
	 * 缓存 key:name, value:UserInfo
	 */
	private Map<String, UserInfo> cache = new ConcurrentHashMap<>();

	@Override
	public boolean existKey(String name) {
		return cache.containsKey(name);
	}

	@Override
	public boolean existValue(UserInfo userInfo) {
		return cache.containsValue(userInfo);
	}

	@Override
	public void save(String name, UserInfo userInfo) {
		cache.put(name, userInfo);
	}

	@Override
	public UserInfo get(String name) {
		return cache.get(name);
	}

	@Override
	public void remove(String name) {
		cache.remove(name);
	}

}
