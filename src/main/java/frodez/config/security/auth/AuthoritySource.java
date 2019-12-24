package frodez.config.security.auth;

import frodez.config.security.settings.SecurityProperties;
import frodez.constant.enums.user.PermissionType;
import frodez.constant.settings.PropertyKey;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.model.user.Permission;
import frodez.util.common.StrUtil;
import frodez.util.common.StreamUtil;
import frodez.util.common.StreamUtil.MapBuilder;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.PropertyUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
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
	 * 权限缓存(按url区分)
	 */
	private Map<String, Collection<ConfigAttribute>> urlCache;

	/**
	 * 权限缓存(按url和请求方式区分)
	 */
	private Map<String, Map<PermissionType, Collection<ConfigAttribute>>> urlTypeCache;

	/**
	 * 更新权限信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	public void refresh() {
		synchronized (this) {
			defaultDeniedRoles = null;
			allCache = null;
			urlCache = null;
			urlTypeCache = null;
			init();
		}
	}

	/**
	 * 初始化
	 * @author Frodez
	 * @date 2019-02-17
	 */
	@PostConstruct
	private void init() {
		if (defaultDeniedRoles != null || allCache != null || urlCache != null || urlTypeCache != null) {
			return;
		}
		//设置默认无权限角色
		defaultDeniedRoles = List.of(new SecurityConfig(ContextUtil.bean(SecurityProperties.class).getAuth().getDeniedRole()));
		//设置所有权限的缓存
		List<Permission> permissions = ContextUtil.bean(PermissionMapper.class).selectAll();
		allCache = StreamUtil.list(permissions, (iter) -> new SecurityConfig(iter.getName()));
		//设置按url区分的权限缓存
		String basePath = PropertyUtil.get(PropertyKey.Web.BASE_PATH);
		permissions.forEach((iter) -> {
			String url = StrUtil.concat(basePath, iter.getUrl());
			iter.setUrl(url);
		});
		var urlCacheMapBuiler = MapBuilder.<Permission, String, Collection<ConfigAttribute>>instance();
		urlCacheMapBuiler.key((iter) -> iter.getUrl());
		urlCacheMapBuiler.value(iter -> {
			Collection<ConfigAttribute> list = new ArrayList<>();
			list.add(new SecurityConfig(iter.getName()));
			return list;
		});
		urlCacheMapBuiler.merge((Collection<ConfigAttribute> a, Collection<ConfigAttribute> b) -> {
			a.addAll(b);
			return a;
		});
		urlCache = permissions.stream().collect(urlCacheMapBuiler.hashMap());
		//设置按url和请求方式区分的权限缓存
		urlTypeCache = new HashMap<>();
		List<String> urls = permissions.stream().map((iter) -> iter.getUrl()).distinct().collect(Collectors.toList());
		for (String url : urls) {
			//按请求方式区分
			Map<PermissionType, Collection<ConfigAttribute>> typeMap = new EnumMap<>(PermissionType.class);
			Function<Permission, ConfigAttribute> mapper = (iter) -> new SecurityConfig(iter.getName());
			//将非ALL类型的请求方式先行处理
			for (PermissionType type : PermissionType.values()) {
				if (type != PermissionType.ALL) {
					List<ConfigAttribute> typeConfigs = permissions.stream().filter((iter) -> iter.getUrl().equals(url) && iter.getType().equals(type
						.getVal())).map(mapper).collect(Collectors.toList());
					typeMap.put(type, typeConfigs);
				}
			}
			//然后处理ALL类型的请求方式,把ALL类型的请求方式转换为其他所有类型的请求方式直接填入
			List<ConfigAttribute> allConfigs = permissions.stream().filter((iter) -> iter.getUrl().equals(url) && PermissionType.ALL.getVal().equals(
				iter.getType())).map(mapper).collect(Collectors.toList());
			for (Entry<PermissionType, Collection<ConfigAttribute>> entry : typeMap.entrySet()) {
				Collection<ConfigAttribute> configs = entry.getValue();
				configs.addAll(allConfigs);
				entry.setValue(configs.stream().distinct().collect(Collectors.toList()));
			}
			//加入按url和请求方式区分的权限缓存
			urlTypeCache.put(url, typeMap);
		}
		Assert.notNull(defaultDeniedRoles, "defaultDeniedRoles must not be null");
		Assert.notNull(allCache, "allCache must not be null");
		Assert.notNull(urlCache, "urlCache must not be null");
		Assert.notNull(urlTypeCache, "urlTypeCache must not be null");
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
			switch (HttpMethod.resolve(invocation.getHttpRequest().getMethod())) {
				case GET : {
					return urlTypeCache.get(url).get(PermissionType.GET);
				}
				case POST : {
					return urlTypeCache.get(url).get(PermissionType.POST);
				}
				case DELETE : {
					return urlTypeCache.get(url).get(PermissionType.DELETE);
				}
				case PUT : {
					return urlTypeCache.get(url).get(PermissionType.PUT);
				}
				default : {
					return urlCache.get(url);
				}
			}
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
