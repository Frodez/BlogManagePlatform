package frodez.service.cache.vm.impl;

import frodez.dao.result.user.UserInfo;
import frodez.service.cache.vm.facade.UserIdCache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class UserIdMapCache implements UserIdCache {

	/**
	 * 缓存 key:name, value:UserInfo
	 */
	private Map<Long, UserInfo> cache = new ConcurrentHashMap<>();

	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public boolean existKey(Long userId) {
		return cache.containsKey(userId);
	}

	@Override
	public boolean existValue(UserInfo userInfo) {
		return cache.containsValue(userInfo);
	}

	@Override
	public void save(Long userId, UserInfo userInfo) {
		cache.put(userId, userInfo);
	}

	@Override
	public UserInfo get(Long userId) {
		return cache.get(userId);
	}

	@Override
	public void remove(Long userId) {
		cache.remove(userId);
	}

}
