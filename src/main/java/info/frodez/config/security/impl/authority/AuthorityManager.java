package info.frodez.config.security.impl.authority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import info.frodez.config.security.settings.SecurityProperties;

/**
 * 权限匹配管理器
 * @author Frodez
 * @date 2018-12-03
 */
@Component
public class AuthorityManager implements AccessDecisionManager {

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

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
	public void decide(Authentication authentication, Object object,
		Collection<ConfigAttribute> attributes)
		throws AccessDeniedException, InsufficientAuthenticationException {
		// 如果是免验证路径,则直接放行
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		if ((properties.getAuth().getBasePath() + properties.getAuth().getPermitAllPath())
			.equals(request.getRequestURI())) {
			return;
		}
		if (CollectionUtils.isEmpty(authentication.getAuthorities())) {
			throw new AccessDeniedException("无访问权限!");
		}
		List<String> attributeList = attributes.stream().map(ConfigAttribute::getAttribute)
			.collect(Collectors.toList());
		// 当包含无访问权限时,直接驳回
		if (attributeList.contains(properties.getAuth().getDeniedRole())) {
			throw new AccessDeniedException("无访问权限!");
		}
		List<String> authorityList = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority).collect(Collectors.toList());
		for (String attribute : attributeList) {
			if (authorityList.contains(attribute)) {
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
