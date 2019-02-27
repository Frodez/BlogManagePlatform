package frodez.config.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import frodez.config.security.settings.SecurityProperties;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class TokenManager {

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	/**
	 * 算法
	 */
	private Algorithm algorithm;

	/**
	 * 签发者
	 */
	private String issuer;

	/**
	 * 过期时长(毫秒)
	 */
	private Long expiration;

	/**
	 * 声明
	 */
	private String claim;

	@PostConstruct
	private void init() {
		algorithm = Algorithm.HMAC256(properties.getJwt().getSecret());
		issuer = properties.getJwt().getIssuer();
		expiration = properties.getJwt().getExpiration() * 1000;
		claim = properties.getJwt().getAuthorityClaim();
	}

	/**
	 * 生成token
	 * @author Frodez
	 * @param UserDetails 用户信息
	 * @date 2018-11-21
	 */
	public String generate(UserDetails user) {
		try {
			Long now = System.currentTimeMillis();
			return JWT.create().withIssuer(issuer).withIssuedAt(new Date(now)).withExpiresAt(new Date(now + expiration))
				.withSubject(user.getUsername()).withArrayClaim(claim, AuthorityUtil.get(user)).sign(algorithm);
		} catch (Exception e) {
			throw new RuntimeException(e);
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
			Long now = System.currentTimeMillis();
			return JWT.create().withIssuer(issuer).withIssuedAt(new Date(now)).withExpiresAt(new Date(now + expiration))
				.withSubject(username).withArrayClaim(claim, authorities.toArray(String[]::new)).sign(algorithm);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 验证token
	 * @author Frodez
	 * @param token
	 * @param expired是否验证超时
	 * @date 2018-11-21
	 */
	public UserDetails verify(String token, boolean expired) {
		DecodedJWT jwt = null;
		if (expired) {
			//前面已经将exp置为合适的过期时间了,这里只需要判断其是否超过当前时间即可.
			jwt = JWT.require(algorithm).acceptExpiresAt(0).withIssuer(issuer).build().verify(token);
		} else {
			jwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
		}
		return new User(jwt.getSubject(), "N/A", AuthorityUtil.get(jwt.getClaim(claim).asArray(String.class)));
	}

}
