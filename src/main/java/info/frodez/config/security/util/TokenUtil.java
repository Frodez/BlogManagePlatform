package info.frodez.config.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import info.frodez.config.security.settings.SecurityProperties;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * token工具类
 * @author Frodez
 * @date 2018-11-14
 */
@Component
public class TokenUtil {

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	/**
	 * 生成token
	 * @author Frodez
	 * @param UserDetails 用户信息
	 * @date 2018-11-21
	 */
	public String generate(UserDetails user) {
		try {
			Long systemTime = System.currentTimeMillis();
			return JWT.create().withIssuer(properties.getJwt().getIssuer()).withIssuedAt(new Date(systemTime))
				.withExpiresAt(new Date(systemTime + properties.getJwt().getExpiration() * 1000))
				.withSubject(user.getUsername())
				.withArrayClaim(properties.getJwt().getAuthorityClaim(), AuthorityUtil.getAuthorities(user))
				.sign(Algorithm.HMAC256(properties.getJwt().getSecret()));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 生成token
	 * @author Frodez
	 * @param UserDetails 用户信息
	 * @date 2018-11-21
	 */
	public String generate(String username, List<String> authorities) {
		try {
			Long systemTime = System.currentTimeMillis();
			return JWT.create().withIssuer(properties.getJwt().getIssuer()).withIssuedAt(new Date(systemTime))
				.withExpiresAt(new Date(systemTime + properties.getJwt().getExpiration() * 1000)).withSubject(username)
				.withArrayClaim(properties.getJwt().getAuthorityClaim(), authorities.stream().toArray(String[]::new))
				.sign(Algorithm.HMAC256(properties.getJwt().getSecret()));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 验证token,失败时返回null
	 * @author Frodez
	 * @param token
	 * @date 2018-11-21
	 */
	public UserDetails verify(String token) {
		try {
			if (token == null) {
				return null;
			}
			DecodedJWT jwt = JWT.require(Algorithm.HMAC256(properties.getJwt().getSecret()))
				.withIssuer(properties.getJwt().getIssuer()).build().verify(token);
			return new User(jwt.getSubject(), "N/A", AuthorityUtil
				.createGrantedAuthorities(jwt.getClaim(properties.getJwt().getAuthorityClaim()).asArray(String.class)));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取request中的token,如果为空或者前缀不符合设置,均返回空.
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public String getRealToken(HttpServletRequest request) {
		String token = request.getHeader(properties.getJwt().getHeader());
		if (token == null || !token.startsWith(properties.getJwt().getTokenPrefix())) {
			return null;
		}
		return token.substring(properties.getJwt().getTokenPrefix().length());
	}

}
