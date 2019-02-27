package frodez.config.aop.request.checker.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import frodez.config.aop.request.checker.facade.ManualChecker;
import frodez.constant.setting.DefTime;
import org.springframework.stereotype.Component;

/**
 * 阻塞型重复请求检查HASHMAP实现
 * @author Frodez
 * @date 2019-01-21
 */
@Component("repeatGuavaChecker")
public class ManualGuavaChecker implements ManualChecker {

	/**
	 * 垃圾收集间隔(毫秒)
	 */
	private static final int GC_INTERVAL = 60000;

	private static final Cache<String, Boolean> CACHE = CacheBuilder.newBuilder().expireAfterAccess(GC_INTERVAL,
		DefTime.UNIT).build();

	@Override
	public boolean check(String key) {
		return CACHE.getIfPresent(key) != null;
	}

	@Override
	public void lock(String key) {
		CACHE.put(key, true);
	}

	@Override
	public void free(String key) {
		CACHE.invalidate(key);
	}

}
