package frodez.service.cache.impl.user;

import frodez.dao.model.result.user.UserBaseInfo;
import frodez.service.cache.facade.user.UserCache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component("userMapCache")
public class UserMapCache implements UserCache {

	private Map<Long, UserBaseInfo> cache = new ConcurrentHashMap<>();

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public UserBaseInfo get(Long id) {
		return cache.get(id);
	}

	@Override
	public void save(Long id, UserBaseInfo user) {
		cache.put(id, user);
	}

	@Override
	public void remove(Long id) {
		cache.remove(id);
	}

}
