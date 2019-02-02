package frodez.config.aop.request.checker.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import frodez.config.aop.request.checker.facade.TimeoutChecker;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/**
 * 自动超时型重复请求检查GUAVACACHE实现(此实现需经测试验证)
 * @author Frodez
 * @date 2019-01-21
 */
@Component("timeoutGuavaChecker")
public class TimeoutGuavaChecker implements TimeoutChecker {

	/**
	 * 垃圾收集间隔(秒)
	 */
	private static final int GC_INTERVAL = 60;

	Cache<String, Long> cache = CacheBuilder.newBuilder().expireAfterAccess(GC_INTERVAL, TimeUnit.SECONDS).build();

	@Override
	public boolean check(String key) {
		long now = System.currentTimeMillis();
		Long timestamp = cache.getIfPresent(key);
		if (timestamp == null) {
			return false;
		} else if (timestamp < now) {
			cache.invalidate(key);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void lock(String key, long timeout) {
		cache.put(key, timeout + System.currentTimeMillis());
	}

}
