//package info.frodez.config.swagger;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.ResponseEntity;
//
//import com.fasterxml.classmate.TypeResolver;
//
//import info.frodez.config.security.settings.SecurityProperties;
//import info.frodez.util.result.Result;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.ApiKey;
//import springfox.documentation.service.AuthorizationScope;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.SecurityReference;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@EnableSwagger2
//@Configuration
//public class SwaggerConfig {
//
//	@Autowired
//	private SecurityProperties properties;
//
//	@Bean
//	public Docket petApi() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.select()
//				.apis(RequestHandlerSelectors.basePackage("info.frodez.controller"))
//				.paths(PathSelectors.any())
//				.build()
//				.apiInfo(apiInfo())
//				.pathMapping("/api")
//				.directModelSubstitute(LocalDate.class, String.class)
//				.genericModelSubstitutes(ResponseEntity.class)
//				.additionalModels(new TypeResolver().resolve(Result.class))
//				.useDefaultResponseMessages(false)
//				.securitySchemes(Arrays.asList(apiKey()))
//				.securityContexts(Arrays.asList(securityContext()))
//				.enableUrlTemplating(false);
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder()
//				.title("Api Documentation")
//				.description("Api Documentation")
//				.contact(new Contact("Frodez", "https://github.com/Frodez/BlogManagePlatform", ""))
//				.version("1.0")
//				.build();
//	}
//
//	private ApiKey apiKey() {
//		return new ApiKey(properties.getJwt().getTokenPrefix(), properties.getJwt().getHeader(), "header");
//	}
//
//	private SecurityContext securityContext() {
//		return SecurityContext.builder()
//				.securityReferences(defaultAuth())
//				.forPaths(PathSelectors.regex("/api/.*")) // 注意要与Restful API路径一致
//				.build();
//	}
//
//	private List<SecurityReference> defaultAuth() {
//		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//		authorizationScopes[0] = authorizationScope;
//		return Arrays.asList(new SecurityReference(properties.getJwt().getTokenPrefix(), authorizationScopes));
//	}
//
//}
