package frodez.service.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import frodez.constant.setting.DefaultTime;
import org.springframework.stereotype.Component;

@Component
public class CommonGuavaService {

	/**
	 * 垃圾收集间隔(毫秒)
	 */
	private static final int GC_INTERVAL = 3600000;

	/**
	 * boolean缓存
	 */
	private static final Cache<String, Boolean> BOOL_CACHE = CacheBuilder.newBuilder().expireAfterAccess(GC_INTERVAL,
		DefaultTime.UNIT).build();

	/**
	 * boolean缓存
	 */
	private static final Cache<String, Byte> BYTE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(GC_INTERVAL,
		DefaultTime.UNIT).build();

	/**
	 * boolean缓存
	 */
	private static final Cache<String, Integer> INT_CACHE = CacheBuilder.newBuilder().expireAfterAccess(GC_INTERVAL,
		DefaultTime.UNIT).build();

	/**
	 * boolean缓存
	 */
	private static final Cache<String, Long> LONG_CACHE = CacheBuilder.newBuilder().expireAfterAccess(GC_INTERVAL,
		DefaultTime.UNIT).build();

	/**
	 * boolean缓存
	 */
	private static final Cache<String, Double> DOUBLE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(GC_INTERVAL,
		DefaultTime.UNIT).build();

	/**
	 * boolean缓存
	 */
	private static final Cache<String, String> STRING_CACHE = CacheBuilder.newBuilder().expireAfterAccess(GC_INTERVAL,
		DefaultTime.UNIT).build();

	/**
	 * boolean缓存
	 */
	private static final Cache<String, Object> OBJECT_CACHE = CacheBuilder.newBuilder().expireAfterAccess(GC_INTERVAL,
		DefaultTime.UNIT).build();

	public Boolean getBool(String key) {
		return BOOL_CACHE.getIfPresent(key);
	}

	public Byte getByte(String key) {
		return BYTE_CACHE.getIfPresent(key);
	}

	public Integer getInt(String key) {
		return INT_CACHE.getIfPresent(key);
	}

	public Long getLong(String key) {
		return LONG_CACHE.getIfPresent(key);
	}

	public Double getDouble(String key) {
		return DOUBLE_CACHE.getIfPresent(key);
	}

	public String getString(String key) {
		return STRING_CACHE.getIfPresent(key);
	}

	public Object getObject(String key) {
		return OBJECT_CACHE.getIfPresent(key);
	}

	public void removeBool(String key) {
		BOOL_CACHE.invalidate(key);
	}

	public void removeByte(String key) {
		BYTE_CACHE.invalidate(key);
	}

	public void removeInt(String key) {
		INT_CACHE.invalidate(key);
	}

	public void removeLong(String key) {
		LONG_CACHE.invalidate(key);
	}

	public void removeDouble(String key) {
		DOUBLE_CACHE.invalidate(key);
	}

	public void removeString(String key) {
		STRING_CACHE.invalidate(key);
	}

	public void removeObject(String key) {
		OBJECT_CACHE.invalidate(key);
	}

}
