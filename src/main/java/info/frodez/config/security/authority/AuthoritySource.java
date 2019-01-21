package info.frodez.config.security.authority;

import info.frodez.config.security.settings.SecurityProperties;
import info.frodez.constant.user.PermissionTypeEnum;
import info.frodez.dao.model.user.Permission;
import info.frodez.service.user.IUserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
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
	private IUserService userAuthorityService;

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	/**
	 * 根据url和请求方式,获取对应的权限
	 * @author Frodez
	 * @date 2018-12-13
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		List<Permission> permissions = userAuthorityService.getAllPermissions().parseList(Permission.class);
		FilterInvocation invocation = (FilterInvocation) object;
		// 这里的url是截去根路径后的url
		String url = invocation.getRequestUrl();
		Collection<ConfigAttribute> attributes = new ArrayList<>();
		// 根据不同请求方式获取对应权限
		switch (HttpMethod.resolve(invocation.getHttpRequest().getMethod())) {
			case GET : {
				attributes.addAll(permissions.stream().filter((iter) -> {
					return (iter.getType() == PermissionTypeEnum.ALL.getValue()
						|| iter.getType() == PermissionTypeEnum.GET.getValue()) && iter.getUrl().equals(url);
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList()));
				break;
			}
			case POST : {
				attributes.addAll(permissions.stream().filter((iter) -> {
					return (iter.getType() == PermissionTypeEnum.ALL.getValue()
						|| iter.getType() == PermissionTypeEnum.POST.getValue()) && iter.getUrl().equals(url);
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList()));
				break;
			}
			case DELETE : {
				attributes.addAll(permissions.stream().filter((iter) -> {
					return (iter.getType() == PermissionTypeEnum.ALL.getValue()
						|| iter.getType() == PermissionTypeEnum.DELETE.getValue()) && iter.getUrl().equals(url);
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList()));
				break;
			}
			case PUT : {
				attributes.addAll(permissions.stream().filter((iter) -> {
					return (iter.getType() == PermissionTypeEnum.ALL.getValue()
						|| iter.getType() == PermissionTypeEnum.PUT.getValue()) && iter.getUrl().equals(url);
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList()));
				break;
			}
			default : {
				attributes.addAll(permissions.stream().filter((iter) -> {
					return iter.getType() == PermissionTypeEnum.ALL.getValue() || iter.getUrl().equals(url);
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList()));
				break;
			}
		}
		//如果未获取权限,则添加无访问权限角色
		if (CollectionUtils.isEmpty(attributes)) {
			attributes.add(new SecurityConfig(properties.getAuth().getDeniedRole()));
		}
		return attributes;
	}

	/**
	 * 获取所有的权限
	 * @author Frodez
	 * @date 2018-12-13
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		List<Permission> permissions = userAuthorityService.getAllPermissions().parseList(Permission.class);
		Collection<ConfigAttribute> attributes = new ArrayList<>();
		attributes.addAll(permissions.stream().map((iter) -> {
			return new SecurityConfig(iter.getName());
		}).collect(Collectors.toList()));
		return attributes;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
