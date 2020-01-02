package frodez.service.cache.impl.config;

import frodez.dao.mapper.config.SettingMapper;
import frodez.service.cache.facade.config.SettingCache;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("settingMapCache")
public class SettingMapCache implements SettingCache {

	private SettingMapper settingMapper;

	private Map<String, Set<Long>> cache = new ConcurrentHashMap<>();

	/**
	 * 初始化缓存<br>
	 * 本缓存key数量不会改变,只有value的值可能会经常改变。<br>
	 * @param settingMapper
	 */
	@Autowired
	public SettingMapCache(SettingMapper settingMapper) {
		this.settingMapper = settingMapper;
		cache.putAll(settingMapper.getSettingRoles());
	}

	@Override
	public synchronized void clear() {
		cache.clear();
		cache.putAll(settingMapper.getSettingRoles());
	}

	@Override
	public synchronized void refresh(Long roleId, List<String> settings) {
		for (Entry<String, Set<Long>> entry : cache.entrySet()) {
			if (settings.contains(entry.getKey())) {
				entry.getValue().add(roleId);
			} else {
				entry.getValue().remove(roleId);
			}
		}
	}

	@Override
	public boolean pass(Long roleId, String setting) {
		Set<Long> roles = cache.get(setting);
		if (roles == null) {
			throw new IllegalArgumentException(setting + "设置不存在!");
		}
		return roles.contains(roleId);
	}

	@Override
	public boolean reject(Long roleId, String setting) {
		Set<Long> roles = cache.get(setting);
		if (roles == null) {
			throw new IllegalArgumentException(setting + "设置不存在!");
		}
		return !roles.contains(roleId);
	}

}
