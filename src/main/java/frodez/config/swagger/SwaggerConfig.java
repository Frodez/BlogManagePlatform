package frodez.config.swagger;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.fasterxml.classmate.TypeResolver;
import frodez.config.security.settings.SecurityProperties;
import frodez.util.result.Result;
import frodez.util.spring.properties.PropertiesUtil;
import frodez.util.spring.properties.PropertyKey;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置
 * @author Frodez
 * @date 2019-01-06
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

	/**
	 * spring参数配置
	 */
	@Autowired
	private PropertiesUtil springProperties;

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties securityProperties;

	/**
	 * swagger参数配置
	 */
	@Autowired
	private SwaggerProperties swaggerProperties;

	/**
	 * 主配置
	 * @author Frodez
	 * @date 2019-01-06
	 */
	@Bean
	public Docket petApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
			.apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
			.paths(PathSelectors.any()).build().apiInfo(apiInfo())
			.pathMapping(springProperties.get(PropertyKey.Web.BASE_PATH))
			.directModelSubstitute(LocalDate.class, String.class)
			.genericModelSubstitutes(ResponseEntity.class)
			.additionalModels(new TypeResolver().resolve(Result.class)).useDefaultResponseMessages(false)
			.securitySchemes(Arrays.asList(apiKey())).securityContexts(Arrays.asList(securityContext()))
			.enableUrlTemplating(false);
	}

	/**
	 * 配置说明
	 * @author Frodez
	 * @date 2019-01-06
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(swaggerProperties.getTitle())
			.description(swaggerProperties.getDescription())
			.contact(new Contact(swaggerProperties.getAuthor(), swaggerProperties.getDocUrl(),
				swaggerProperties.getEmail()))
			.version("1.0").build();
	}

	/**
	 * 配置token
	 * @author Frodez
	 * @date 2019-01-06
	 */
	private ApiKey apiKey() {
		return new ApiKey(securityProperties.getJwt().getTokenPrefix(),
			securityProperties.getJwt().getHeader(), "header");
	}

	/**
	 * 配置访问路径
	 * @author Frodez
	 * @date 2019-01-06
	 */
	private SecurityContext securityContext() {
		// 注意要与RestfulAPI路径一致
		return SecurityContext.builder().securityReferences(defaultAuth())
			.forPaths(PathSelectors.regex(springProperties.get(PropertyKey.Web.BASE_PATH))).build();
	}

	/**
	 * 默认验证配置
	 * @author Frodez
	 * @date 2019-01-06
	 */
	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(
			new SecurityReference(securityProperties.getJwt().getTokenPrefix(), authorizationScopes));
	}

}
