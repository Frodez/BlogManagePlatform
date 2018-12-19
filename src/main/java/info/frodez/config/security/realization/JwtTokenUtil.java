package info.frodez.config.security.realization;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
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

	private static final String CLAIM_AUTHORITIES = "authorities";

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
            Algorithm algorithm = Algorithm.HMAC256(config.getJwt().getSecret());
            Long systemTime = System.currentTimeMillis();
            return JWT.create()
                    .withIssuer(config.getJwt().getIssuer())
                    .withIssuedAt(new Date(systemTime))
                    .withExpiresAt(new Date(systemTime + config.getJwt().getExpiration() * 1000))
                    .withSubject(user.getUsername())
                    .withArrayClaim(CLAIM_AUTHORITIES, AuthorityUtil.getAuthorities(user))
                    .sign(algorithm);
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
            Algorithm algorithm = Algorithm.HMAC256(config.getJwt().getSecret());
            Long systemTime = System.currentTimeMillis();
            return JWT.create()
                    .withIssuer(config.getJwt().getIssuer())
                    .withIssuedAt(new Date(systemTime))
                    .withExpiresAt(new Date(systemTime + config.getJwt().getExpiration() * 1000))
                    .withSubject(username)
                    .withArrayClaim(CLAIM_AUTHORITIES, authorities.stream().toArray(String[]::new))
                    .sign(algorithm);
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
        if (token == null) {
            return null;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(config.getJwt().getSecret());
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(config.getJwt().getIssuer()).build();
            DecodedJWT jwt = verifier.verify(token);
            return new User(jwt.getSubject(), "N/A", 
            	AuthorityUtil.createGrantedAuthorities(jwt.getClaim(CLAIM_AUTHORITIES).asArray(String.class)));
        } catch (Exception e) {
            return null;
        }
    }
	
}
