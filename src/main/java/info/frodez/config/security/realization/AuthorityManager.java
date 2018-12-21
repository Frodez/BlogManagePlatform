package info.frodez.config.security.realization;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * 权限匹配管理器
 * @author Frodez
 * @date 2018-12-03
 */
@Component
public class AuthorityManager implements AccessDecisionManager {

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
	public void decide(Authentication authentication,
			Object object, Collection<ConfigAttribute> attributes)
					throws AccessDeniedException, InsufficientAuthenticationException {
		//如果请求的资源没有找到权限则放行，表示该资源为公共资源，都可以访问
		if(CollectionUtils.isEmpty(attributes)) {
			return;
		}
		if(CollectionUtils.isEmpty(authentication.getAuthorities())) {
			throw new AccessDeniedException("无访问权限!");
		}
		List<String> attributeList = attributes
				.stream().map(ConfigAttribute::getAttribute)
				.collect(Collectors.toList());
		List<String> authorityList = authentication
				.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		for(String attribute : attributeList) {
			if(authorityList.contains(attribute)) {
				return;
			}
		}
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
