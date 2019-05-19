package frodez.util.http;

import frodez.config.security.settings.SecurityProperties;
import frodez.constant.settings.PropertyKey;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.util.common.StrUtil;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.PropertyUtil;
import java.util.Collection;
import java.util.HashSet;
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
 * 路径匹配器<br>
 * 本匹配器匹配需验证路径和免验证路径。<br>
 * @author Frodez
 * @date 2019-03-27
 */
@Component
@DependsOn(value = { "propertyUtil", "contextUtil" })
public class Matcher {

	/**
	 * 需验证路径
	 */
	private static Set<String> needVerifyPaths = new HashSet<>();

	/**
	 * 免验证路径
	 */
	private static Set<String> permitPaths = new HashSet<>();

	/**
	 * 基础免验证路径
	 */
	private static Set<String> basePermitPaths = new HashSet<>();

	/**
	 * PatchMatcher
	 */
	private static PathMatcher matcher;

	@PostConstruct
	private void init() {
		matcher = ContextUtil.get(PathMatcher.class);
		String basePath = PropertyUtil.get(PropertyKey.Web.BASE_PATH);
		//预处理所有允许的路径,这里的路径还是antMatcher风格的路径
		for (String path : ContextUtil.get(SecurityProperties.class).getAuth().getPermitAllPath()) {
			String realPath = StrUtil.concat(basePath, path);
			//先加入到允许路径中
			permitPaths.add(realPath);
			basePermitPaths.add(realPath);
		}
		String errorPath = StrUtil.concat(basePath, PropertyUtil.get(PropertyKey.Web.ERROR_PATH));
		//错误路径也加入到允许路径中
		permitPaths.add(errorPath);
		basePermitPaths.add(errorPath);
		//找出所有端点的url
		BeanFactoryUtils.beansOfTypeIncludingAncestors(ContextUtil.context(), HandlerMapping.class, true, false)
			.values().stream().filter((iter) -> {
				return iter instanceof RequestMappingHandlerMapping;
			}).map((iter) -> {
				return ((RequestMappingHandlerMapping) iter).getHandlerMethods().entrySet();
			}).flatMap(Collection::stream).forEach((entry) -> {
				//获取该端点的url
				String requestUrl = StrUtil.concat(PropertyUtil.get(PropertyKey.Web.BASE_PATH), entry.getKey()
					.getPatternsCondition().getPatterns().iterator().next());
				//直接判断该url是否需要验证,如果与免验证路径匹配则加入不需要验证路径,否则加入需要验证路径中
				for (String path : basePermitPaths) {
					if (matcher.match(path, requestUrl)) {
						permitPaths.add(requestUrl);
						return;
					}
				}
				needVerifyPaths.add(requestUrl);
			});
		Assert.notNull(matcher, "matcher must not be null");
		Assert.notNull(needVerifyPaths, "needVerifyUrls must not be null");
		Assert.notNull(permitPaths, "permitUrls must not be null");
		Assert.notNull(basePermitPaths, "basePermitPaths must not be null");
		//如果是release或者prod环境
		if (ContextUtil.get(PermissionMapper.class).selectAll().stream().filter((iter) -> {
			return isPermitAllPath(StrUtil.concat(PropertyUtil.get(PropertyKey.Web.BASE_PATH), iter.getUrl()));
		}).count() != 0 && (PropertyUtil.isRelease() || PropertyUtil.isProd())) {
			throw new RuntimeException("处于release或者prod环境下时,不能在免验证路径下配置权限!");
		}
	}

	/**
	 * 判断路径是否需要验证<br>
	 * 路径获取方式:<br>
	 * <code>
	 * HttpServletRequest request = ...;
	 * String uri = request.getRequestURI();
	 * </code><br>
	 * <strong>true为需要验证,false为不需要验证</strong><br>
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static boolean needVerify(String uri) {
		return needVerifyPaths.contains(uri);
	}

	/**
	 * 判断是否为免验证路径<br>
	 * 路径获取方式:<br>
	 * <code>
	 * HttpServletRequest request = ...;
	 * String uri = request.getRequestURI();
	 * </code><br>
	 * <strong>true为需要验证,false为不需要验证</strong><br>
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public static boolean isPermitAllPath(String uri) {
		//对于存在的路径,这里就可以直接判断
		if (permitPaths.contains(uri)) {
			return true;
		}
		//对于可能出现的错误路径,交由matcher判断
		for (String path : basePermitPaths) {
			if (matcher.match(path, uri)) {
				return true;
			}
		}
		return false;
	}

}
