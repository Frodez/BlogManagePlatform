package frodez.config.security.auth;

import frodez.config.security.settings.SecurityProperties;
import frodez.config.security.util.Matcher;
import frodez.util.common.EmptyUtil;
import frodez.util.common.StreamUtil;
import frodez.util.spring.ContextUtil;
import java.util.Collection;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 权限匹配管理器
 * @author Frodez
 * @date 2018-12-03
 */
@Component
@DependsOn("contextUtil")
public class AuthorityManager implements AccessDecisionManager {

	private ConfigAttribute defaultDeniedRole;

	private void clear() {
		defaultDeniedRole = null;
	}

	@PostConstruct
	private void init() {
		SecurityProperties properties = ContextUtil.bean(SecurityProperties.class);
		defaultDeniedRole = new SecurityConfig(properties.getAuth().getDeniedRole());
		Assert.notNull(defaultDeniedRole, "defaultDeniedRole must not be null");
	}

	/**
	 * 更新权限信息
	 * @author Frodez
	 * @date 2019-03-17
	 */
	public void refresh() {
		synchronized (this) {
			clear();
			init();
		}
	}

	/**
	 * 判定是否拥有权限<br>
	 * authentication是UserDetailsServiceImpl中添加到GrantedAuthority中的权限信息.<br>
	 * object包含客户端请求的request信息，可转换为HttpServletRequest,方法如下:<br>
	 * request = ((FilterInvocation) object).getHttpRequest()<br>
	 * attributes是DatabaseSecurityMetadataSource的getAttributes方法的返回值.<br>
	 * 如果用户不具有请求的url的权限,抛出AccessDeniedException.<br>
	 * @author Frodez
	 * @date 2018-12-03
	 */
	@Override
	public void decide(Authentication auth, Object object, Collection<ConfigAttribute> permissions) throws AccessDeniedException,
		InsufficientAuthenticationException {
		FilterInvocation invocation = (FilterInvocation) object;
		if (!Matcher.needVerify(invocation.getHttpRequest())) {
			// 如果是免验证路径,则直接放行,因为免验证路径下为了防止报错,设置了一个默认的无访问权限
			return;
		}
		//如果用户不带有权限,说明用户信息可能有问题,必须直接驳回
		//详情见frodez.config.security.filter.TokenFilter.doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		//和frodez.config.security.user.UserDetailsServiceImpl.loadUserByUsername(String username)方法
		if (EmptyUtil.yes(auth.getAuthorities())) {
			throw new AccessDeniedException("无访问权限!");
		}
		// 当包含无访问权限时,直接驳回(此时只有无访问权限一个权限)
		if (permissions.contains(defaultDeniedRole)) {
			throw new AccessDeniedException("无访问权限!");
		}
		Set<String> auths = StreamUtil.set(auth.getAuthorities(), GrantedAuthority::getAuthority);
		for (ConfigAttribute permission : permissions) {
			if (auths.contains(permission.getAttribute())) {
				return;
			}
		}
		// 当token携带权限与资源所需访问权限不符时,驳回
		throw new AccessDeniedException("无访问权限!");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
