package frodez.config.aop.request.checker.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import frodez.config.aop.request.checker.facade.RepeatChecker;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/**
 * 阻塞型重复请求检查HASHMAP实现
 * @author Frodez
 * @date 2019-01-21
 */
@Component("repeatGuavaChecker")
public class RepeatGuavaChecker implements RepeatChecker {

	private static final int GC_INTERVAL = 60000;

	Cache<String, Boolean> cache =
		CacheBuilder.newBuilder().expireAfterAccess(GC_INTERVAL, TimeUnit.MILLISECONDS).build();

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
