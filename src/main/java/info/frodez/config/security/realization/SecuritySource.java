package info.frodez.config.security.realization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import info.frodez.constant.user.PermissionTypeEnum;
import info.frodez.dao.model.user.Permission;
import info.frodez.service.IUserAuthorityService;

/**
 * 获取权限资源
 * @author Frodez
 * @date 2018-12-04
 */
@Component
public class SecuritySource implements FilterInvocationSecurityMetadataSource {
	
	private static final String METHOD_GET = "GET";
	
	private static final String METHOD_POST = "POST";
	
	private static final String METHOD_DELETE = "DELETE";
	
	private static final String METHOD_PUT = "PUT";

	@Autowired
	private IUserAuthorityService userAuthorityService;
	
	/**
	 * 根据url和请求方式,获取对应的权限
	 * @author Frodez
	 * @date 2018-12-13
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		List<Permission> permissions = userAuthorityService.getAllPermissions().getListData(Permission.class);
		FilterInvocation invocation = (FilterInvocation) object;
		String url = invocation.getRequestUrl();
		String method = invocation.getHttpRequest().getMethod();
		//根据不同请求方式获取对应权限
		switch (method) {
			case METHOD_GET : {
				Collection<ConfigAttribute> attributes = new ArrayList<>();
				attributes.addAll(permissions.stream().filter((iter) -> {
					return iter.getType() == PermissionTypeEnum.ALL.getValue() 
						|| iter.getType() == PermissionTypeEnum.GET.getValue()
						|| iter.getUrl().equals(url);
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList()));
				return attributes;
			}
			case METHOD_POST : {
				Collection<ConfigAttribute> attributes = new ArrayList<>();
				attributes.addAll(permissions.stream().filter((iter) -> {
					return iter.getType() == PermissionTypeEnum.ALL.getValue() 
						|| iter.getType() == PermissionTypeEnum.POST.getValue()
						|| iter.getUrl().equals(url);
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList()));
				return attributes;
			}
			case METHOD_DELETE : {
				Collection<ConfigAttribute> attributes = new ArrayList<>();
				attributes.addAll(permissions.stream().filter((iter) -> {
					return iter.getType() == PermissionTypeEnum.ALL.getValue() 
						|| iter.getType() == PermissionTypeEnum.DELETE.getValue()
						|| iter.getUrl().equals(url);
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList()));
				return attributes;
			}
			case METHOD_PUT : {
				Collection<ConfigAttribute> attributes = new ArrayList<>();
				attributes.addAll(permissions.stream().filter((iter) -> {
					return iter.getType() == PermissionTypeEnum.ALL.getValue() 
						|| iter.getType() == PermissionTypeEnum.PUT.getValue()
						|| iter.getUrl().equals(url);
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList()));
				return attributes;
			}
			default : {
				Collection<ConfigAttribute> attributes = new ArrayList<>();
				attributes.addAll(permissions.stream().filter((iter) -> {
					return iter.getType() == PermissionTypeEnum.ALL.getValue()
						|| iter.getUrl().equals(url);
				}).map((iter) -> {
					return new SecurityConfig(iter.getName());
				}).collect(Collectors.toList()));
				return attributes;
			}
		}
	}

	/**
	 * 获取所有的权限
	 * @author Frodez
	 * @date 2018-12-13
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
