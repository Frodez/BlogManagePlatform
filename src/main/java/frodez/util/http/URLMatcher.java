package frodez.util.http;

import frodez.config.security.settings.SecurityProperties;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.util.constant.setting.PropertyKey;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.PropertyUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * url匹配器<br>
 * 本匹配器只匹配系统中已存在的需验证url和免验证url。<br>
 * 如果需要额外的匹配功能,请使用PathMatcher。
 * @author Frodez
 * @date 2019-03-27
 */
@Component
@DependsOn(value = { "propertyUtil", "contextUtil" })
public class URLMatcher {

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
		PathMatcher matcher = ContextUtil.get(PathMatcher.class);
		String basePath = PropertyUtil.get(PropertyKey.Web.BASE_PATH);
		List<String> permitPaths = new ArrayList<>();
		for (String path : ContextUtil.get(SecurityProperties.class).getAuth().getPermitAllPath()) {
			permitUrls.add(basePath + path);
			permitPaths.add(basePath + path);
		}
		String errorPath = basePath + PropertyUtil.get(PropertyKey.Web.ERROR_PATH);
		permitUrls.add(errorPath);
		BeanFactoryUtils.beansOfTypeIncludingAncestors(ContextUtil.context(), HandlerMapping.class, true, false)
			.values().stream().filter((iter) -> {
				return iter instanceof RequestMappingHandlerMapping;
			}).map((iter) -> {
				return ((RequestMappingHandlerMapping) iter).getHandlerMethods().entrySet();
			}).flatMap(Collection::stream).forEach((entry) -> {
				String requestUrl = PropertyUtil.get(PropertyKey.Web.BASE_PATH) + entry.getKey().getPatternsCondition()
					.getPatterns().iterator().next();
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
		Assert.notNull(needVerifyUrls, "needVerifyUrls must not be null");
		Assert.notNull(permitUrls, "permitUrls must not be null");
		if (ContextUtil.get(PermissionMapper.class).selectAll().stream().filter((iter) -> {
			return permitUrls.contains(PropertyUtil.get(PropertyKey.Web.BASE_PATH) + iter.getUrl());
		}).count() != 0) {
			throw new RuntimeException("不能在免验证路径下配置权限!");
		}
	}

	/**
	 * 判断uri是否需要验证<br>
	 * uri获取方式:<br>
	 * <code>
	 * HttpServletRequest request = ...;
	 * String uri = request.getRequestURI();
	 * </code><br>
	 * <strong>true为需要验证,false为不需要验证</strong><br>
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static boolean needVerify(String uri) {
		return needVerifyUrls.contains(uri);
	}

	/**
	 * 判断uri是否为免验证路径<br>
	 * uri获取方式:<br>
	 * <code>
	 * HttpServletRequest request = ...;
	 * String uri = request.getRequestURI();
	 * </code><br>
	 * <strong>true为需要验证,false为不需要验证</strong><br>
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public static boolean isPermitAllPath(String uri) {
		return permitUrls.contains(uri);
	}

}
