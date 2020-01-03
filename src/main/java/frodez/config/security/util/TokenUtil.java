package frodez.config.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import frodez.config.security.settings.SecurityProperties;
import frodez.dao.model.result.user.UserEndpointDetail;
import frodez.util.spring.ContextUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * token工具类
 * @author Frodez
 * @date 2018-11-14
 */
@Component
@DependsOn("contextUtil")
public class TokenUtil {

	/**
	 * 算法
	 */
	private static Algorithm algorithm;

	/**
	 * 验证过期
	 */
	private static JWTVerifier verifier;

	/**
	 * 不验证过期
	 */
	private static JWTVerifier expiredVerifier;

	/**
	 * 签发者
	 */
	private static String issuer;

	/**
	 * 是否过期，true为会过期
	 */
	private static boolean expired = false;

	/**
	 * 过期时长(毫秒)
	 */
	private static Long expiration;

	/**
	 * 声明
	 */
	private static String authorityClaim;

	/**
	 * HttpHeader名称
	 */
	private static String header;

	/**
	 * token前缀
	 */
	private static String tokenPrefix;

	/**
	 * token前缀长度
	 */
	private static int tokenPrefixLength;

	@PostConstruct
	private void init() {
		SecurityProperties properties = ContextUtil.bean(SecurityProperties.class);
		algorithm = Algorithm.HMAC256(properties.getJwt().getSecret());
		issuer = properties.getJwt().getIssuer();
		expiration = properties.getJwt().getExpiration() * 1000;
		expired = expiration > 0;
		authorityClaim = properties.getJwt().getAuthorityClaim();
		header = properties.getJwt().getHeader();
		tokenPrefix = properties.getJwt().getTokenPrefix();
		tokenPrefixLength = tokenPrefix.length();
		verifier = JWT.require(algorithm).withIssuer(issuer).build();
		expiredVerifier = JWT.require(algorithm).acceptExpiresAt(0).withIssuer(issuer).build();
		Assert.notNull(algorithm, "algorithm must not be null");
		Assert.notNull(issuer, "issuer must not be null");
		Assert.notNull(expiration, "expiration must not be null");
		Assert.notNull(authorityClaim, "authorityClaim must not be null");
		Assert.notNull(header, "header must not be null");
		Assert.notNull(tokenPrefix, "tokenPrefix must not be null");
		Assert.notNull(verifier, "verifier must not be null");
		Assert.notNull(expiredVerifier, "expireAbleVerifier must not be null");
	}

	/**
	 * 基本builder
	 * @author Frodez
	 * @date 2019-12-29
	 */
	private static Builder baseBuilder() {
		long now = System.currentTimeMillis();
		Builder builder = JWT.create();
		builder.withIssuer(issuer);
		builder.withIssuedAt(new Date(now));
		if (expired) {
			builder.withExpiresAt(new Date(now + expiration));
		}
		return builder;
	}

	/**
	 * 生成token
	 * @author Frodez
	 * @param UserEndpointDetail 用户信息
	 * @date 2018-11-21
	 */
	public static String generate(UserEndpointDetail user) {
		Builder builder = baseBuilder();
		builder.withSubject(user.getUser().getName());
		builder.withArrayClaim(authorityClaim, AuthorityUtil.get(user.getEndpoints()));
		return builder.sign(algorithm);
	}

	/**
	 * 验证token
	 * @author Frodez
	 * @param token
	 * @date 2018-11-21
	 */
	public static UserDetails verify(String token) {
		//前面已经将exp置为合适的过期时间了,这里只需要判断其是否超过当前时间即可.
		DecodedJWT jwt = expired ? expiredVerifier.verify(token) : verifier.verify(token);
		return new User(jwt.getSubject(), "N/A", AuthorityUtil.make(jwt.getClaim(authorityClaim).asArray(String.class)));
	}

	/**
	 * 验证token,且一定考虑过期
	 * @author Frodez
	 * @date 2018-11-21
	 */
	public static UserDetails verifyWithNoExpired(String token) {
		DecodedJWT jwt = verifier.verify(token);
		return new User(jwt.getSubject(), "N/A", AuthorityUtil.make(jwt.getClaim(authorityClaim).asArray(String.class)));
	}

	/**
	 * 获取request中的token,如果为空或者前缀不符合设置,均返回null.
	 * @see frodez.config.security.util.TokenUtil#getFullToken(HttpServletRequest)
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getRealToken(HttpServletRequest request) {
		String token = request.getHeader(header);
		if (token == null || !token.startsWith(tokenPrefix)) {
			return null;
		}
		return token.substring(tokenPrefixLength);
	}

	/**
	 * 获取request中的token,不做任何处理.
	 * @see frodez.config.security.util.TokenUtil#getRealToken(HttpServletRequest)
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getFullToken(HttpServletRequest request) {
		return request.getHeader(header);
	}

}
