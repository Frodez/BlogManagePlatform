package frodez.config.security.util;

import frodez.config.security.settings.SecurityProperties;
import frodez.constant.settings.PropertyKey;
import frodez.dao.mapper.permission.EndpointMapper;
import frodez.dao.model.table.permission.Endpoint;
import frodez.util.common.StrUtil;
import frodez.util.common.StreamUtil;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.MVCUtil;
import frodez.util.spring.PropertyUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

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
		SecurityProperties securityProperties = ContextUtil.bean(SecurityProperties.class);
		matcher = ContextUtil.bean(PathMatcher.class);
		String basePath = PropertyUtil.get(PropertyKey.Web.BASE_PATH);
		//预处理所有允许的路径,这里的路径还是antMatcher风格的路径
		for (String path : securityProperties.getAuth().getPermitAllPath()) {
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
		MVCUtil.requestMappingHandlerMappingStream().map((iter) -> iter.getHandlerMethods().entrySet()).flatMap(Collection::stream).forEach((
			entry) -> {
			//获取该端点的路径
			String requestPath = StrUtil.concat(basePath, entry.getKey().getPatternsCondition().getPatterns().iterator().next());
			//直接判断该路径是否需要验证,如果与免验证路径匹配则加入不需要验证路径,否则加入需要验证路径中
			for (String path : basePermitPaths) {
				if (matcher.match(path, requestPath)) {
					permitPaths.add(requestPath);
					return;
				}
			}
			needVerifyPaths.add(requestPath);
		});
		Assert.notNull(matcher, "matcher must not be null");
		Assert.notNull(needVerifyPaths, "needVerifyPaths must not be null");
		Assert.notNull(permitPaths, "permitPaths must not be null");
		Assert.notNull(basePermitPaths, "basePermitPaths must not be null");
		checkCorrectPermissions(securityProperties);
	}

	/**
	 * 仅用于init()方法，在必要时检查权限正确性
	 * @author Frodez
	 * @date 2019-05-19
	 */
	private static void checkCorrectPermissions(SecurityProperties securityProperties) {
		if (securityProperties.getAuth().getPermissionCheck()) {

			List<Endpoint> incorrectPermissions = StreamUtil.filterList(ContextUtil.bean(EndpointMapper.class).selectAll(), (iter) -> isPermitAllPath(
				StrUtil.concat(PropertyUtil.get(PropertyKey.Web.BASE_PATH), iter.getPath())));
			if (!incorrectPermissions.isEmpty()) {
				String message = incorrectPermissions.stream().map((item) -> {
					StringBuilder builder = new StringBuilder();
					builder.append("路径:").append(item.getPath()).append(", 名称:").append(item.getName()).append(", 描述: ").append(item
						.getDescription());
					return builder.toString();
				}).collect(Collectors.joining("\n"));
				throw new RuntimeException("\n权限正确性检查发现存在错误权限!\n" + message);
			}
		}
	}

	/**
	 * 判断路径是否需要验证<br>
	 * 相比于isPermitAllPath方法,将不需要验证的路径全部缓存,加快速度。<br>
	 * <strong> 可以使用本方法的情况下应该使用本方法!<br>
	 * true为需要验证,false为不需要验证<br>
	 * </strong>
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static boolean needVerify(HttpServletRequest request) {
		return needVerifyPaths.contains(request.getRequestURI());
	}

	/**
	 * 判断路径是否需要验证<br>
	 * 相比于isPermitAllPath方法,将不需要验证的路径全部缓存,加快速度。<br>
	 * 路径获取方式:<br>
	 * <code>
	 * HttpServletRequest request = ...;
	 * String uri = request.getRequestURI();
	 * </code><br>
	 * <strong> 可以使用本方法的情况下应该使用本方法!<br>
	 * true为需要验证,false为不需要验证<br>
	 * </strong>
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public static boolean needVerify(String uri) {
		return needVerifyPaths.contains(uri);
	}

	/**
	 * 判断是否为免验证路径<br>
	 * <strong>true为需要验证,false为不需要验证</strong><br>
	 * @author Frodez
	 * @date 2019-03-10
	 */
	public static boolean isPermitAllPath(HttpServletRequest request) {
		return isPermitAllPath(request.getRequestURI());
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
