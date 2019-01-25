package frodez.config.aop.request.checker.impl;

import frodez.config.aop.request.checker.facade.TimeoutChecker;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自动超时型重复请求检查HASHMAP实现(此实现需经测试验证)
 * @author Frodez
 * @date 2019-01-21
 */
@Slf4j
@Component
public class TimeoutHashMapChecker implements TimeoutChecker {

	/**
	 * 垃圾回收间隔时间
	 */
	private static final int GALLERY_COLLECTION_TIMES = 1000000;

	/**
	 * 垃圾回收窗口占比(倒数)
	 */
	private static final int RANGE_DIVISION = 10000;

	/**
	 * 垃圾回收窗口时间
	 */
	private static final int WINDOW_TIMES = GALLERY_COLLECTION_TIMES / RANGE_DIVISION;

	/**
	 * GC时间限值
	 */
	private static final int WARN_GC_TIMES = 100;

	private static AtomicBoolean lock = new AtomicBoolean(false);

	private static final Map<String, Long> checker = new ConcurrentHashMap<>();

	private static void galleryCollction(long now) {
		if (now % GALLERY_COLLECTION_TIMES < WINDOW_TIMES) {
			return;
		}
		if (lock.compareAndSet(false, true)) {
			for (Map.Entry<String, Long> entry : checker.entrySet()) {
				if (entry.getValue() < now - GALLERY_COLLECTION_TIMES) {
					checker.remove(entry.getKey());
				}
			}
		}
		lock.set(false);
		if (System.currentTimeMillis() - now > WARN_GC_TIMES) {
			log.warn("[galleryCollction]gc时间过长!");
		}
	}

	@Override
	public boolean check(String key) {
		long now = System.currentTimeMillis();
		try {
			Long timestamp = checker.get(key);
			if (timestamp == null) {
				return false;
			} else if (timestamp < now) {
				checker.remove(key);
				return false;
			} else {
				return true;
			}
		} finally {
			galleryCollction(now);
		}
	}

	@Override
	public void lock(String key, long timeout) {
		checker.put(key, timeout + System.currentTimeMillis());
	}

}
