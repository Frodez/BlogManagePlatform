package info.frodez.config.security.impl.util;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import info.frodez.config.security.settings.SecurityProperties;

/**
 * jwt工具类
 * @author Frodez
 * @date 2018-11-14
 */
@Component
public class JwtTokenUtil {

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties config;

	/**
	 * 生成token
	 * @author Frodez
	 * @param UserDetails 用户信息
	 * @date 2018-11-21
	 */
	public String generate(UserDetails user) {
		try {
			Long systemTime = System.currentTimeMillis();
			return JWT.create().withIssuer(config.getJwt().getIssuer()).withIssuedAt(new Date(systemTime))
				.withExpiresAt(new Date(systemTime + config.getJwt().getExpiration() * 1000))
				.withSubject(user.getUsername())
				.withArrayClaim(config.getJwt().getAuthorityClaim(), AuthorityUtil.getAuthorities(user))
				.sign(Algorithm.HMAC256(config.getJwt().getSecret()));
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
			return JWT.create().withIssuer(config.getJwt().getIssuer()).withIssuedAt(new Date(systemTime))
				.withExpiresAt(new Date(systemTime + config.getJwt().getExpiration() * 1000))
				.withSubject(username).withArrayClaim(config.getJwt().getAuthorityClaim(),
					authorities.stream().toArray(String[]::new))
				.sign(Algorithm.HMAC256(config.getJwt().getSecret()));
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
			DecodedJWT jwt = JWT.require(Algorithm.HMAC256(config.getJwt().getSecret()))
				.withIssuer(config.getJwt().getIssuer()).build().verify(token);
			return new User(jwt.getSubject(), "N/A", AuthorityUtil.createGrantedAuthorities(
				jwt.getClaim(config.getJwt().getAuthorityClaim()).asArray(String.class)));
		} catch (Exception e) {
			return null;
		}
	}

}
