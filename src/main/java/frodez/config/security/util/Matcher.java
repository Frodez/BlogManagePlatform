package frodez.config.security.util;

import frodez.config.security.annotation.VerifyStrategy;
import frodez.config.security.annotation.VerifyStrategy.VerifyStrategyEnum;
import frodez.config.security.settings.SecurityProperties;
import frodez.constant.settings.PropertyKey;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.model.user.Permission;
import frodez.util.common.StrUtil;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.PropertyUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
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
		SecurityProperties securityProperties = ContextUtil.get(SecurityProperties.class);
		matcher = ContextUtil.get(PathMatcher.class);
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
		BeanFactoryUtils.beansOfTypeIncludingAncestors(ContextUtil.context(), HandlerMapping.class, true, false)
			.values().stream().filter((iter) -> {
				return iter instanceof RequestMappingHandlerMapping;
			}).map((iter) -> {
				return ((RequestMappingHandlerMapping) iter).getHandlerMethods().entrySet();
			}).flatMap(Collection::stream).forEach((entry) -> {
				//获取该端点的路径
				String requestPath = StrUtil.concat(basePath, entry.getKey().getPatternsCondition().getPatterns()
					.iterator().next());
				HandlerMethod handlerMethod = entry.getValue();
				//如果方法上标注了@VerifyStrategy注解,则该路径的配置由注解接管
				VerifyStrategy verifyStrategy = handlerMethod.getMethodAnnotation(VerifyStrategy.class);
				if (verifyStrategy != null) {
					if (verifyStrategy.value() == VerifyStrategyEnum.FREE_PASS) {
						permitPaths.add(requestPath);
					} else {
						needVerifyPaths.add(requestPath);
					}
					return;
				}
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
			List<Permission> incorrectPermissions = ContextUtil.get(PermissionMapper.class).selectAll().stream().filter(
				(iter) -> {
					return isPermitAllPath(StrUtil.concat(PropertyUtil.get(PropertyKey.Web.BASE_PATH), iter.getUrl()));
				}).collect(Collectors.toList());
			if (!incorrectPermissions.isEmpty()) {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < incorrectPermissions.size() - 1; i++) {
					Permission incorrect = incorrectPermissions.get(i);
					builder.append("路径:").append(incorrect.getUrl()).append(", 名称:").append(incorrect.getName()).append(
						", 描述: ").append(incorrect.getDescription()).append("\n");
				}
				Permission incorrect = incorrectPermissions.get(incorrectPermissions.size() - 1);
				builder.append("路径:").append(incorrect.getUrl()).append(", 名称:").append(incorrect.getName()).append(
					", 描述: ").append(incorrect.getDescription());
				throw new RuntimeException("\n权限正确性检查发现存在错误权限!\n" + builder.toString());
			}
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
