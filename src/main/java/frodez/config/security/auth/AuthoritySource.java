package frodez.config.security.auth;

import frodez.config.security.settings.SecurityProperties;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.model.user.Permission;
import frodez.util.constant.user.PermissionTypeEnum;
import frodez.util.spring.context.ContextUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

/**
 * 获取权限资源
 * @author Frodez
 * @date 2018-12-04
 */
@Component
@DependsOn("contextUtil")
public class AuthoritySource implements FilterInvocationSecurityMetadataSource {

	/**
	 * 用户授权服务
	 */
	@Autowired
	private PermissionMapper permissionMapper;

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
	private Map<String, Map<PermissionTypeEnum, Collection<ConfigAttribute>>> urlTypeCache;

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
		SecurityProperties properties = ContextUtil.get(SecurityProperties.class);
		defaultDeniedRoles = Arrays.asList(new SecurityConfig(properties.getAuth().getDeniedRole()));
		if (allCache == null) {
			List<Permission> permissions = permissionMapper.selectAll();
			allCache = permissions.stream().map((iter) -> {
				return new SecurityConfig(iter.getName());
			}).collect(Collectors.toList());
			if (allCache == null) {
				throw new RuntimeException("获取所有权限失败!");
			}
			urlCache = permissions.stream().collect(Collectors.toMap(Permission::getUrl, iter -> {
				Collection<ConfigAttribute> list = new ArrayList<>();
				list.add(new SecurityConfig(iter.getName()));
				return list;
			}, (Collection<ConfigAttribute> a, Collection<ConfigAttribute> b) -> {
				a.addAll(b);
				return a;
			}));
			urlTypeCache = new HashMap<>();
			List<String> urls = permissions.stream().map(Permission::getUrl).distinct().collect(Collectors.toList());
			for (String url : urls) {
				Map<PermissionTypeEnum, Collection<ConfigAttribute>> typeMap = new EnumMap<>(PermissionTypeEnum.class);
				for (PermissionTypeEnum type : PermissionTypeEnum.values()) {
					if (type != PermissionTypeEnum.ALL) {
						Collection<ConfigAttribute> configs = permissions.stream().filter((iter) -> {
							return iter.getUrl().equals(url) && iter.getType().equals(type.getVal());
						}).map((iter) -> {
							return new SecurityConfig(iter.getName());
						}).collect(Collectors.toList());
						typeMap.put(type, configs);
					}
				}
				List<SecurityConfig> allConfigs = permissions.stream().filter((iter) -> {
					return iter.getUrl().equals(url) && iter.getType().equals(PermissionTypeEnum.ALL.getVal());
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList());
				for (Entry<PermissionTypeEnum, Collection<ConfigAttribute>> entry : typeMap.entrySet()) {
					Collection<ConfigAttribute> configs = entry.getValue();
					configs.addAll(allConfigs);
					entry.setValue(configs.stream().distinct().collect(Collectors.toList()));
				}
				urlTypeCache.put(url, typeMap);
			}
		}
		Objects.requireNonNull(defaultDeniedRoles);
		Objects.requireNonNull(allCache);
		Objects.requireNonNull(urlCache);
		Objects.requireNonNull(urlTypeCache);
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
					return urlTypeCache.get(url).get(PermissionTypeEnum.GET);
				}
				case POST : {
					return urlTypeCache.get(url).get(PermissionTypeEnum.POST);
				}
				case DELETE : {
					return urlTypeCache.get(url).get(PermissionTypeEnum.DELETE);
				}
				case PUT : {
					return urlTypeCache.get(url).get(PermissionTypeEnum.PUT);
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
