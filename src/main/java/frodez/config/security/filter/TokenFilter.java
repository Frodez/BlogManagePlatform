package frodez.config.security.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import frodez.config.security.util.Matcher;
import frodez.config.security.util.TokenUtil;
import frodez.service.cache.facade.user.IdTokenCache;
import frodez.util.beans.result.Result;
import frodez.util.http.ServletUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * jwt验证过滤器
 * @author Frodez
 * @date 2018-11-21
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

	@Autowired
	@Qualifier("idTokenRedisCache")
	private IdTokenCache idTokenCache;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException,
		IOException {
		if (Matcher.needVerify(request)) {
			String token = TokenUtil.getRealToken(request);
			UserDetails user;
			try {
				user = TokenUtil.verify(token);
			} catch (TokenExpiredException e) {
				//如果token超时失效,这里不删除token,而是直接返回,并告诉客户端token失效,让客户端重新登陆.
				ServletUtil.writeJson(response, Result.expired());
				return;
			}
			if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				if (!idTokenCache.exist(token)) {
					//如果缓存中不存在用户,则说明被下线
					ServletUtil.writeJson(response, Result.notLogin("该用户已被下线,请重新登录"));
					return;
				}
				//如果成功取出信息且上下文中无验证信息,则设置验证信息
				//这里要设置权限,和frodez.config.security.user.UserDetailsServiceImpl.loadUserByUsername(String username)
				//和frodez.config.security.auth.AuthorityManager.decide(Authentication auth, Object object, Collection<ConfigAttribute> permissions)对应
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}

}
