package frodez.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import frodez.config.security.settings.SecurityProperties;
import frodez.util.beans.result.Result;
import frodez.util.constant.setting.PropertyKey;
import frodez.util.spring.PropertyUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
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
		List<ResponseMessage> responseMessageList = getGlobalResponseMessage();
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage(
			swaggerProperties.getBasePackage())).paths(PathSelectors.any()).build().apiInfo(apiInfo()).pathMapping(
				PropertyUtil.get(PropertyKey.Web.BASE_PATH)).directModelSubstitute(LocalDate.class, String.class)
			.genericModelSubstitutes(ResponseEntity.class).additionalModels(new TypeResolver().resolve(Result.class))
			.useDefaultResponseMessages(false).securitySchemes(Arrays.asList(apiKey())).securityContexts(Arrays.asList(
				securityContext())).enableUrlTemplating(false).globalResponseMessage(RequestMethod.GET,
					responseMessageList).globalResponseMessage(RequestMethod.POST, responseMessageList)
			.globalResponseMessage(RequestMethod.PUT, responseMessageList).globalResponseMessage(RequestMethod.DELETE,
				responseMessageList).globalOperationParameters(getGlobalOperationParameters());
	}

	private List<ResponseMessage> getGlobalResponseMessage() {
		List<ResponseMessage> list = new ArrayList<>();
		Map<HttpStatus, List<Result.ResultEnum>> map = new HashMap<>();
		for (Result.ResultEnum item : Result.ResultEnum.values()) {
			if (map.containsKey(item.getStatus())) {
				map.get(item.getStatus()).add(item);
			} else {
				List<Result.ResultEnum> enumList = new ArrayList<>();
				enumList.add(item);
				map.put(item.getStatus(), enumList);
			}
		}
		for (Entry<HttpStatus, List<Result.ResultEnum>> entry : map.entrySet()) {
			String message = String.join(" | ", entry.getValue().stream().map((iter) -> {
				return iter.getDesc() + ",自定义状态码:" + iter.getVal();
			}).collect(Collectors.toList()));
			ResponseMessage responseMessage = new ResponseMessageBuilder().code(entry.getKey().value()).message(message)
				.responseModel(new ModelRef("Result")).build();
			list.add(responseMessage);
		}
		return list;
	}

	private List<Parameter> getGlobalOperationParameters() {
		List<Parameter> list = new ArrayList<>();
		list.add(new ParameterBuilder().name(securityProperties.getJwt().getAuthorityClaim()).description(
			"token,除免验证url外均必填").required(false).parameterType("header").modelRef(new ModelRef("string")).build());
		return list;
	}

	/**
	 * 配置说明
	 * @author Frodez
	 * @date 2019-01-06
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(swaggerProperties.getTitle()).description(swaggerProperties.getDescription())
			.contact(new Contact(swaggerProperties.getAuthor(), swaggerProperties.getDocUrl(), swaggerProperties
				.getEmail())).version("1.0").build();
	}

	/**
	 * 配置token
	 * @author Frodez
	 * @date 2019-01-06
	 */
	private ApiKey apiKey() {
		return new ApiKey(securityProperties.getJwt().getTokenPrefix(), securityProperties.getJwt().getHeader(),
			"header");
	}

	/**
	 * 配置访问路径
	 * @author Frodez
	 * @date 2019-01-06
	 */
	private SecurityContext securityContext() {
		// 注意要与RestfulAPI路径一致
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex(PropertyUtil
			.get(PropertyKey.Web.BASE_PATH))).build();
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
		return Arrays.asList(new SecurityReference(securityProperties.getJwt().getTokenPrefix(), authorizationScopes));
	}

}
