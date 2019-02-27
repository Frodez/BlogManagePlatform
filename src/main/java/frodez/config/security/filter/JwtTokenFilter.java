package frodez.config.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import frodez.config.security.settings.SecurityProperties;
import frodez.config.security.util.TokenManager;
import frodez.constant.setting.DefaultResult;
import frodez.util.http.ServletUtil;
import frodez.util.result.ResultEnum;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

	/**
	 * token工具类
	 */
	@Autowired
	private TokenManager tokenManager;

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws ServletException, IOException {
		String authToken = null;
		try {
			//建议url不要带入任何path类型参数,以提高性能!
			if (properties.needVerify(request.getRequestURI())) {
				authToken = properties.getRealToken(request);
				if (authToken != null) {
					// 将携带的token还原成用户信息
					UserDetails user = tokenManager.verify(authToken, true);
					if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							user, null, user.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			}
			chain.doFilter(request, response);
		} catch (JWTVerificationException e) {
			if (e instanceof TokenExpiredException) {
				//如果token超时失效,这里不删除token,而是告诉客户端token失效,让客户端重新登陆.
				ServletUtil.writeJson(response, ResultEnum.EXPIRED.getStatus(), DefaultResult.EXPIRED_STRING);
			}
		}

	}

}
