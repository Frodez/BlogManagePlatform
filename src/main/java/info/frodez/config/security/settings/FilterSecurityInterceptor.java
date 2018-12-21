package info.frodez.config.security.settings;

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

import info.frodez.config.security.realization.AuthorityManager;
import info.frodez.config.security.realization.SecuritySource;

/**
 * 自定义权限拦截器
 * @author Frodez
 * @date 2018-12-05
 */
@Component
public class FilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

	@Autowired
	private SecuritySource securityMetadataSource;

	@Autowired
	private AuthorityManager accessDecisionManager;

	@Autowired
	public void setAccessDecisionManager() {
		super.setAccessDecisionManager(accessDecisionManager);
	}

	@Override
	public SecuritySource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}

	public void invoke(FilterInvocation fi) throws IOException, ServletException {
		//fi里面有一个被拦截的url
		//里面调用MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
		//再调用MyAccessDecisionManager的decide方法来校验用户的权限是否足够
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			//执行下一个拦截器
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
