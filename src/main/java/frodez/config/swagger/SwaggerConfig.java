package frodez.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import frodez.config.security.settings.SecurityProperties;
import frodez.constant.settings.PropertyKey;
import frodez.util.beans.result.Result;
import frodez.util.spring.PropertyUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置
 * @author Frodez
 * @date 2019-01-06
 */
@EnableSwagger2
@Configuration
@Profile({ "dev", "test" })
public class SwaggerConfig {

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties security;

	/**
	 * swagger参数配置
	 */
	@Autowired
	private SwaggerProperties swagger;

	/**
	 * 主配置
	 * @author Frodez
	 * @date 2019-01-06
	 */
	@Bean
	public Docket petApi() {
		Docket docket = baseConfig();
		infoConfig(docket);
		typeConfig(docket);
		secretConfig(docket);
		modelConfig(docket);
		return docket;
	}

	/**
	 * 基本配置
	 * @author Frodez
	 * @date 2019-12-04
	 */
	private Docket baseConfig() {
		ApiSelectorBuilder selectorBuilder = new Docket(DocumentationType.SWAGGER_2).select();
		selectorBuilder.apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()));
		selectorBuilder.paths(PathSelectors.any());
		return selectorBuilder.build();
	}

	/**
	 * 说明配置
	 * @author Frodez
	 * @date 2019-01-06
	 */
	private void infoConfig(Docket docket) {
		ApiInfoBuilder infoBuilder = new ApiInfoBuilder();
		infoBuilder.title(swagger.getTitle());
		infoBuilder.description(swagger.getDescription());
		Contact contact = new Contact(swagger.getAuthor(), swagger.getDocUrl(), swagger.getEmail());
		infoBuilder.contact(contact);
		infoBuilder.version(swagger.getAppVersion());
		docket.apiInfo(infoBuilder.build());
	}

	/**
	 * 类型映射配置
	 * @author Frodez
	 * @date 2019-12-04
	 */
	private void typeConfig(Docket docket) {
		docket.directModelSubstitute(LocalDate.class, String.class);
		docket.genericModelSubstitutes(ResponseEntity.class);
		docket.additionalModels(new TypeResolver().resolve(Result.class));
	}

	/**
	 * 安全配置
	 * @author Frodez
	 * @date 2019-12-04
	 */
	private void secretConfig(Docket docket) {
		docket.securitySchemes(apiKey());
		docket.securityContexts(securityContext());
	}

	/**
	 * 模型映射配置
	 * @author Frodez
	 * @date 2019-12-04
	 */
	private void modelConfig(Docket docket) {
		List<ResponseMessage> responseMessageList = getGlobalResponseMessage();
		docket.useDefaultResponseMessages(false);
		docket.enableUrlTemplating(false);
		docket.globalResponseMessage(RequestMethod.GET, responseMessageList);
		docket.globalResponseMessage(RequestMethod.POST, responseMessageList);
		docket.globalResponseMessage(RequestMethod.PUT, responseMessageList);
		docket.globalResponseMessage(RequestMethod.DELETE, responseMessageList);
	}

	/**
	 * 全局返回信息
	 * @author Frodez
	 * @date 2019-06-06
	 */
	private List<ResponseMessage> getGlobalResponseMessage() {
		List<ResponseMessage> list = new ArrayList<>();
		Map<HttpStatus, List<Result.ResultEnum>> map = new HashMap<>();
		for (Result.ResultEnum item : Result.ResultEnum.values()) {
			if (item.getStatus() == HttpStatus.OK) {
				//成功的返回信息不设置默认
				continue;
			}
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
			ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder();
			messageBuilder.code(entry.getKey().value());
			messageBuilder.message(message);
			messageBuilder.responseModel(new ModelRef(Result.class.getSimpleName()));
			list.add(messageBuilder.build());
		}
		return list;
	}

	/**
	 * 配置token
	 * @author Frodez
	 * @date 2019-01-06
	 */
	private List<ApiKey> apiKey() {
		ApiKey apiKey = new ApiKey(security.getJwt().getTokenPrefix(), security.getJwt().getHeader(), "header");
		return List.of(apiKey);
	}

	/**
	 * 配置访问路径
	 * @author Frodez
	 * @date 2019-01-06
	 */
	private List<SecurityContext> securityContext() {
		// 注意要与RestfulAPI路径一致
		SecurityContext securityContext = SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex(PropertyUtil.get(
			PropertyKey.Web.BASE_PATH))).build();
		return List.of(securityContext);
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
		return List.of(new SecurityReference(security.getJwt().getTokenPrefix(), authorizationScopes));
	}

}
