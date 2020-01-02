package frodez.config.security.auth;

import frodez.config.security.settings.SecurityProperties;
import frodez.constant.converters.HttpMethodReverter;
import frodez.dao.mapper.permission.EndpointMapper;
import frodez.dao.model.table.permission.Endpoint;
import frodez.util.spring.ContextUtil;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 获取权限资源
 * @author Frodez
 * @date 2018-12-04
 */
@Component
@DependsOn({ "contextUtil", "propertyUtil" })
public class AuthoritySource implements FilterInvocationSecurityMetadataSource {

	/**
	 * 默认无权限角色
	 */
	private Collection<ConfigAttribute> defaultDeniedRoles;

	/**
	 * 权限缓存
	 */
	private Collection<ConfigAttribute> allCache;

	/**
	 * 权限缓存(按url和请求方式区分)
	 */
	private Map<String, Map<HttpMethod, Collection<ConfigAttribute>>> urlTypeCache;

	/**
	 * 更新权限信息,仅在对接口修改时会使用
	 * @author Frodez
	 * @date 2019-03-17
	 */
	public void refresh() {
		synchronized (this) {
			init();
		}
	}

	private void clear() {
		defaultDeniedRoles = null;
		allCache = null;
		urlTypeCache = null;
	}

	/**
	 * 初始化
	 * @author Frodez
	 * @date 2019-02-17
	 */
	@PostConstruct
	private void init() {
		clear();
		List<Endpoint> endpoints = ContextUtil.bean(EndpointMapper.class).selectAll();
		//设置默认无权限角色
		defaultDeniedRoles = List.of(new SecurityConfig(ContextUtil.bean(SecurityProperties.class).getAuth().getDeniedRole()));
		//设置所有权限的缓存
		allCache = endpoints.stream().map((iter) -> new SecurityConfig(iter.getName())).collect(Collectors.toList());
		//设置按url和请求方式区分的权限缓存
		urlTypeCache = new HashMap<>();
		for (Endpoint endpoint : endpoints) {
			Map<HttpMethod, Collection<ConfigAttribute>> urlMap = urlTypeCache.get(endpoint.getPath());
			if (urlMap == null) {
				urlMap = buildNewUrlMap(endpoint);
			} else {
				modifyUrlMap(urlMap, endpoint);
			}
		}
		Assert.notNull(defaultDeniedRoles, "defaultDeniedRoles must not be null");
		Assert.notNull(allCache, "allCache must not be null");
		Assert.notNull(urlTypeCache, "urlTypeCache must not be null");
	}

	private Map<HttpMethod, Collection<ConfigAttribute>> buildNewUrlMap(Endpoint endpoint) {
		Map<HttpMethod, Collection<ConfigAttribute>> urlMap = new EnumMap<>(HttpMethod.class);
		List<HttpMethod> httpMethods = HttpMethodReverter.revert(endpoint.getMethods());
		for (HttpMethod httpMethod : HttpMethod.values()) {
			Collection<ConfigAttribute> attributes = new HashSet<>();
			urlMap.put(httpMethod, attributes);
		}
		for (HttpMethod httpMethod : httpMethods) {
			urlMap.get(httpMethod).add(new SecurityConfig(endpoint.getName()));
		}
		return urlMap;
	}

	private void modifyUrlMap(Map<HttpMethod, Collection<ConfigAttribute>> urlMap, Endpoint endpoint) {
		List<HttpMethod> httpMethods = HttpMethodReverter.revert(endpoint.getMethods());
		for (HttpMethod httpMethod : httpMethods) {
			urlMap.get(httpMethod).add(new SecurityConfig(endpoint.getName()));
		}
	}

	/**
	 * 根据url和请求方式,获取对应的权限
	 * @author Frodez
	 * @date 2018-12-13
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		FilterInvocation invocation = (FilterInvocation) object;
		// 这里的url是截去根路径后的url
		String url = invocation.getHttpRequest().getRequestURI();
		if (urlTypeCache.containsKey(url)) {
			// 根据不同请求方式获取对应权限
			return urlTypeCache.get(url).get(HttpMethod.resolve(invocation.getHttpRequest().getMethod()));
		}
		//如果未获取权限,则添加无访问权限角色
		return defaultDeniedRoles;
	}

	/**
	 * 获取所有的权限(会在启动时执行)
	 * @author Frodez
	 * @date 2018-12-13
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return allCache;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
