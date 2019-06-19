package frodez.config.aop.request.checker.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import frodez.config.aop.request.checker.facade.ManualChecker;
import frodez.config.cache.CacheProperties;
import frodez.constant.settings.DefTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * 阻塞型重复请求检查HASHMAP实现
 * @author Frodez
 * @date 2019-01-21
 */
@Component("repeatGuavaChecker")
@DependsOn("contextUtil")
public class ManualGuavaChecker implements ManualChecker {

	private Cache<String, Boolean> cache;

	@Autowired
	public ManualGuavaChecker(CacheProperties properties) {
		cache = CacheBuilder.newBuilder().expireAfterAccess(properties.getManualGuavaChecker().getTimeout(),
			DefTime.UNIT).build();
	}

	@Override
	public boolean check(String key) {
		return cache.getIfPresent(key) != null;
	}

	@Override
	public void lock(String key) {
		cache.put(key, true);
	}

	@Override
	public void free(String key) {
		cache.invalidate(key);
	}

}
