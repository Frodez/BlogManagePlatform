package frodez.config.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import frodez.config.security.util.TokenManager;
import frodez.util.beans.result.Result;
import frodez.util.http.ServletUtil;
import frodez.util.http.URLMatcher;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * jwt验证过滤器
 * @author Frodez
 * @date 2018-11-21
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws ServletException, IOException {
		//建议url不要带入任何path类型参数,以提高handlemapping性能!
		if (URLMatcher.needVerify(request.getRequestURI())) {
			String authToken = TokenManager.getRealToken(request);
			if (authToken != null) {
				// 将携带的token还原成用户信息
				UserDetails user = null;
				try {
					user = TokenManager.verify(authToken, true);
				} catch (JWTVerificationException e) {
					if (e instanceof TokenExpiredException) {
						//如果token超时失效,这里不删除token,而是告诉客户端token失效,让客户端重新登陆.
						ServletUtil.writeJson(response, Result.expired());
					}
				}
				if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
						null, user.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		chain.doFilter(request, response);
	}

}
