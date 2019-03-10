package frodez.util.http;

import frodez.config.security.settings.SecurityProperties;
import frodez.util.constant.setting.PropertyKey;
import frodez.util.spring.context.ContextUtil;
import frodez.util.spring.properties.PropertyUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
@DependsOn(value = { "propertyUtil", "contextUtil" })
public class URLMatcher {

	private static PathMatcher matcher;

	/**
	 * 需验证url
	 */
	private static Set<String> needVerifyUrls = new HashSet<>();

	/**
	 * 免验证url
	 */
	private static Set<String> permitUrls = new HashSet<>();

	@PostConstruct
	private void init() {
		SecurityProperties securityProperties = ContextUtil.get(SecurityProperties.class);
		matcher = ContextUtil.get(PathMatcher.class);
		String basePath = PropertyUtil.get(PropertyKey.Web.BASE_PATH);
		List<String> permitPaths = new ArrayList<>();
		for (String path : securityProperties.getAuth().getPermitAllPath()) {
			permitUrls.add(basePath + path);
			permitPaths.add(basePath + path);
		}
		String errorPath = basePath + "/error";
		permitUrls.add(errorPath);
		BeanFactoryUtils.beansOfTypeIncludingAncestors(ContextUtil.context(), HandlerMapping.class, true, false)
			.values().stream().filter((iter) -> {
				return iter instanceof RequestMappingHandlerMapping;
			}).map((iter) -> {
				return RequestMappingHandlerMapping.class.cast(iter).getHandlerMethods().entrySet();
			}).flatMap(Collection::stream).forEach((entry) -> {
				String requestUrl = PropertyUtil.get(PropertyKey.Web.BASE_PATH) + entry.getKey().getPatternsCondition()
					.getPatterns().stream().findFirst().get();
				if (requestUrl.equals(errorPath)) {
					return;
				}
				for (String path : permitPaths) {
					if (matcher.match(path, requestUrl)) {
						return;
					}
				}
				needVerifyUrls.add(requestUrl);
			});
	}

	/**
	 * 判断url是否需要验证,url为带有根路径的url<br>
	 * <strong>true为需要验证,false为不需要验证</strong><br>
	 * <strong>建议url不要带入任何path类型参数,以提高性能!</strong>
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static boolean needVerify(String url) {
		return needVerifyUrls.contains(url);
	}

	/**
	 * 判断url是否为免验证路径,url为带有根路径的url<br>
	 * <strong>true为需要验证,false为不需要验证</strong><br>
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public static boolean isPermitAllPath(String url) {
		return permitUrls.contains(url);
	}

	public static boolean match(String pattern, String path) {
		return matcher.match(pattern, path);
	}

}
