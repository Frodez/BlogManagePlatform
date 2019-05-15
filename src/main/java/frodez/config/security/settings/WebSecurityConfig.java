package frodez.config.security.settings;

import frodez.config.security.error.AccessDenied;
import frodez.config.security.error.AuthDenied;
import frodez.config.security.filter.TokenFilter;
import frodez.config.security.settings.SecurityProperties.Cors;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * spring security配置
 * @author Frodez
 * @date 2018-12-02
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private ServerProperties serverProperties;

	/**
	 * 无验证访问控制
	 */
	@Autowired
	private AuthDenied authentication;

	/**
	 * 无权限访问控制
	 */
	@Autowired
	private AccessDenied accessDenied;

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	/**
	 * 验证信息获取服务
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * jwt验证过滤器
	 */
	@Autowired
	private TokenFilter filter;

	/**
	 * 主配置
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		List<String> permitAllPathList = properties.getAuth().getPermitAllPath();
		//开启https
		if (serverProperties.getSsl().isEnabled()) {
			http.requiresChannel().anyRequest().requiresSecure();
		}
		http.cors().and().csrf().disable().exceptionHandling()
			// 无权限时导向noAuthPoint
			.authenticationEntryPoint(authentication).and().exceptionHandling().accessDeniedHandler(accessDenied).and()
			.sessionManagement()
			// 不创建session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
			// 不控制的地址
			.antMatchers(permitAllPathList.toArray(new String[permitAllPathList.size()])).permitAll().antMatchers(
				HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated()
			// 在密码验证过滤器前执行jwt过滤器
			.and().addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).headers().cacheControl(); // 禁止缓存
	}

	/**
	 * 验证信息获取服务配置
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	/**
	 * 密码加密器
	 * @author Frodez
	 * @date 2018-12-04
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 验证管理器
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * 跨域资源配置
	 * @author Frodez
	 * @date 2018-12-04
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		Cors cors = properties.getCors();
		configuration.setAllowedOrigins(cors.getAllowedOrigins());
		configuration.setAllowedMethods(cors.getAllowedMethods());
		configuration.setAllowedHeaders(cors.getAllowedHeaders());
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	/**
	 * 设置登出处理器
	 * @author Frodez
	 * @date 2019-03-27
	 */
	@Bean
	public SecurityContextLogoutHandler securityContextLogoutHandler() {
		return new SecurityContextLogoutHandler();
	}

}
