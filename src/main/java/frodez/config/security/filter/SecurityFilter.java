package frodez.config.security.filter;

import frodez.config.security.auth.AuthorityManager;
import frodez.config.security.auth.AuthoritySource;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
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

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
		ServletException {
		invoke(new FilterInvocation(request, response, chain));
	}

	/**
	 * 自定义权限拦截
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public void invoke(FilterInvocation fi) throws IOException, ServletException {
		// fi里面有一个被拦截的url
		// 里面调用SecuritySource的getAttributes(Object object)这个方法获取fi对应的所有权限
		// 再调用AuthorityManager的decide方法来校验用户的权限是否足够
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			// 执行下一个拦截器
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

}
