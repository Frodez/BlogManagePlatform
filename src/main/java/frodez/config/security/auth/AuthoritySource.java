package frodez.config.security.auth;

import frodez.config.security.settings.SecurityProperties;
import frodez.dao.model.user.Permission;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.common.EmptyUtil;
import frodez.util.constant.user.PermissionTypeEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthoritySource implements FilterInvocationSecurityMetadataSource {

	/**
	 * 用户授权服务
	 */
	@Autowired
	private IAuthorityService authorityService;

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	/**
	 * 权限缓存
	 */
	private List<SecurityConfig> allCache = null;

	/**
	 * 权限缓存(按url区分)
	 */
	private Map<String, List<SecurityConfig>> urlCache = null;

	/**
	 * 权限缓存(按url和请求方式区分)
	 */
	private Map<String, Map<PermissionTypeEnum, List<SecurityConfig>>> urlTypeCache = null;

	/**
	 * 初始化
	 * @author Frodez
	 * @date 2019-02-17
	 */
	private void init() {
		if (allCache == null) {
			List<Permission> permissions = authorityService.getAllPermissions().list(Permission.class);
			allCache = permissions.stream().map((iter) -> {
				return new SecurityConfig(iter.getName());
			}).collect(Collectors.toList());
			if (allCache == null) {
				throw new RuntimeException("获取所有权限失败!");
			}
			urlCache = permissions.stream().collect(Collectors.toMap(Permission::getUrl, iter -> {
				List<SecurityConfig> list = new ArrayList<>();
				list.add(new SecurityConfig(iter.getName()));
				return list;
			}, (List<SecurityConfig> a, List<SecurityConfig> b) -> {
				a.addAll(b);
				return a;
			}));
			urlTypeCache = new HashMap<>();
			List<String> urls = permissions.stream().map(Permission::getUrl).distinct().collect(Collectors.toList());
			for (String url : urls) {
				Map<PermissionTypeEnum, List<SecurityConfig>> typeMap = new HashMap<>();
				for (PermissionTypeEnum type : PermissionTypeEnum.values()) {
					if (type != PermissionTypeEnum.ALL) {
						List<SecurityConfig> configs = permissions.stream().filter((iter) -> {
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
				for (Entry<PermissionTypeEnum, List<SecurityConfig>> entry : typeMap.entrySet()) {
					List<SecurityConfig> configs = entry.getValue();
					configs.addAll(allConfigs);
					entry.setValue(configs.stream().distinct().collect(Collectors.toList()));
				}
				urlTypeCache.put(url, typeMap);
			}
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
		String url = invocation.getRequestUrl();
		Collection<ConfigAttribute> attributes = new ArrayList<>();
		if (urlTypeCache.containsKey(url)) {
			// 根据不同请求方式获取对应权限
			switch (HttpMethod.resolve(invocation.getHttpRequest().getMethod())) {
				case GET : {
					attributes = urlTypeCache.get(url).get(PermissionTypeEnum.GET).stream().collect(Collectors
						.toList());
					break;
				}
				case POST : {
					attributes = urlTypeCache.get(url).get(PermissionTypeEnum.POST).stream().collect(Collectors
						.toList());
					break;
				}
				case DELETE : {
					attributes = urlTypeCache.get(url).get(PermissionTypeEnum.DELETE).stream().collect(Collectors
						.toList());
					break;
				}
				case PUT : {
					attributes = urlTypeCache.get(url).get(PermissionTypeEnum.PUT).stream().collect(Collectors
						.toList());
					break;
				}
				default : {
					attributes = urlCache.get(url).stream().collect(Collectors.toList());
					break;
				}
			}
		}
		//如果未获取权限,则添加无访问权限角色
		if (EmptyUtil.yes(attributes)) {
			attributes.add(new SecurityConfig(properties.getAuth().getDeniedRole()));
		}
		return attributes;
	}

	/**
	 * 获取所有的权限(会在启动时执行)
	 * @author Frodez
	 * @date 2018-12-13
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		init();
		Collection<ConfigAttribute> attributes = new ArrayList<>();
		attributes.addAll(allCache.stream().collect(Collectors.toList()));
		return attributes;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
