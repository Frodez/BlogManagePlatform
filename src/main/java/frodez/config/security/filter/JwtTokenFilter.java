package frodez.config.security.filter;

import frodez.config.security.settings.SecurityProperties;
import frodez.config.security.util.TokenUtil;
import frodez.constant.redis.Redis;
import frodez.service.redis.RedisService;
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
	private TokenUtil tokenUtil;

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	/**
	 * redis服务
	 */
	@Autowired
	private RedisService redisService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws ServletException, IOException {
		if (properties.needVerify(request.getRequestURI())) {
			String authToken = tokenUtil.getRealToken(request);
			if (authToken != null) {
				// 将携带的token还原成用户信息
				UserDetails user = tokenUtil.verify(authToken);
				if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					// 这里将token作为key,userName作为value存入redis,方便之后通过token获取用户信息
					redisService.set(Redis.User.TOKEN + authToken, user.getUsername());
					UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		chain.doFilter(request, response);
	}

}
