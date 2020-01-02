package frodez.service.cache.impl.user;

import frodez.dao.model.table.user.Role;
import frodez.service.cache.facade.user.RoleCache;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("roleMapCache")
public class RoleMapCache implements RoleCache {

	private Map<Long, Role> cache = new ConcurrentHashMap<>();

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public Role get(Long id) {
		return cache.get(id);
	}

	@Override
	public void save(Long id, Role role) {
		cache.put(id, role);
	}

	@Override
	public void save(List<Long> ids, Role role) {
		cache.putAll(ids.stream().collect(Collectors.toMap((item) -> item, (item) -> role)));
	}

	@Override
	public void remove(Long id) {
		cache.remove(id);
	}

}
