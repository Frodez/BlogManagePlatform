package frodez.util.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import frodez.util.beans.pair.KVPair;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.springframework.util.Assert;

/**
 * 正则表达式工具类
 * @author Frodez
 * @date 2019-03-01
 */
public class RegexUtil {

	private static final long MAX_SIZE = 65536;

	private static final Map<String, KVPair<Pattern, Cache<String, Boolean>>> PATTERN_CACHE = new ConcurrentHashMap<>();

	private static KVPair<Pattern, Cache<String, Boolean>> prepare(String regex) {
		KVPair<Pattern, Cache<String, Boolean>> pair = PATTERN_CACHE.get(regex);
		if (pair == null) {
			pair = new KVPair<>();
			pair.setKey(Pattern.compile(regex));
			pair.setValue(CacheBuilder.newBuilder().maximumSize(MAX_SIZE).build());
			PATTERN_CACHE.put(regex, pair);
		}
		return pair;
	}

	/**
	 * 正则表达式判断
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean match(String regex, String target) {
		Assert.notNull(regex, "正则表达式不能为空!");
		Assert.notNull(target, "目标字符串不能为空!");
		KVPair<Pattern, Cache<String, Boolean>> pair = prepare(regex);
		Boolean result = pair.getValue().getIfPresent(target);
		if (result == null) {
			result = pair.getKey().matcher(target).matches();
			pair.getValue().put(target, result);
		}
		return result;
	}

}
