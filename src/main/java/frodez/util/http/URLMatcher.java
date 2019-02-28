package frodez.util.http;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import frodez.config.cache.CacheProperties;
import frodez.config.security.settings.SecurityProperties;
import frodez.constant.setting.DefTime;
import frodez.constant.setting.PropertyKey;
import frodez.util.spring.context.ContextUtil;
import frodez.util.spring.properties.PropertyUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

@Component
@DependsOn(value = { "propertyUtil", "contextUtil" })
public class URLMatcher {

	/**
	 * spring路径匹配器
	 */
	private static PathMatcher matcher;

	private static List<String> permitPaths;

	private static String basePath;

	/**
	 * url匹配缓存
	 */
	private static Cache<String, Boolean> URL_CACHE;

	@PostConstruct
	private void init() {
		SecurityProperties securityProperties = ContextUtil.getBean(SecurityProperties.class);
		CacheProperties cacheProperties = ContextUtil.getBean(CacheProperties.class);
		matcher = ContextUtil.getBean(PathMatcher.class);
		permitPaths = securityProperties.getAuth().getPermitAllPath();
		basePath = PropertyUtil.get(PropertyKey.Web.BASE_PATH);
		URL_CACHE = CacheBuilder.newBuilder().maximumSize(cacheProperties.getUrlMatcher().getMaxSize())
			.expireAfterAccess(cacheProperties.getUrlMatcher().getTimeout(), DefTime.UNIT).build();
	}

	/**
	 * 判断url是否需要验证<br>
	 * <strong>建议url不要带入任何path类型参数,以提高性能!</strong>
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static boolean needVerify(String url) {
		Boolean result = URL_CACHE.getIfPresent(url);
		if (result != null) {
			return result;
		}
		for (String path : permitPaths) {
			if (matcher.match(basePath + path, url)) {
				URL_CACHE.put(url, false);
				return false;
			}
		}
		URL_CACHE.put(url, true);
		return true;
	}

}
