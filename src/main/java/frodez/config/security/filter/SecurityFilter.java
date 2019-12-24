package frodez.config.security.filter;

import frodez.config.security.auth.AuthorityManager;
import frodez.config.security.auth.AuthoritySource;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

/**
 * 自定义权限拦截器
 * @author Frodez
 * @date 2018-12-05
 */
@Component
public class SecurityFilter extends AbstractSecurityInterceptor implements Filter {

	/**
	 * 权限资源
	 */
	@Autowired
	private AuthoritySource securitySource;

	/**
	 * 设置权限匹配管理器
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Autowired
	public void setAccessDecisionManager(AuthorityManager authorityManager) {
		super.setAccessDecisionManager(authorityManager);
	}

	/**
	 * 获取权限资源
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Override
	public AuthoritySource obtainSecurityMetadataSource() {
		return securitySource;
	}

	/**
	 * 自定义权限拦截
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// fi里面有一个被拦截的url
		// 里面调用SecuritySource的getAttributes(Object object)这个方法获取fi对应的所有权限
		// 再调用AuthorityManager的decide方法来校验用户的权限是否足够
		FilterInvocation invocation = new FilterInvocation(request, response, chain);
		InterceptorStatusToken token = super.beforeInvocation(invocation);
		try {
			// 执行下一个拦截器
			invocation.getChain().doFilter(invocation.getRequest(), invocation.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	@Override
	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

}
