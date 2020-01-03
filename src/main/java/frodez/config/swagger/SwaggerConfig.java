package frodez.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import frodez.config.security.settings.SecurityProperties;
import frodez.config.swagger.plugin.SuccessPlugin.SwaggerModel;
import frodez.config.swagger.util.SwaggerUtil;
import frodez.constant.settings.PropertyKey;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.PageData;
import frodez.util.beans.result.Result;
import frodez.util.reflect.ReflectUtil;
import frodez.util.spring.PropertyUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
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
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spi.service.contexts.SecurityContextBuilder;
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
	public Docket enable() {
		Docket docket = baseConfig();
		infoConfig(docket);
		typeConfig(docket);
		secretConfig(docket);
		modelConfig(docket);
		genericTypeNamingStrategyConfig(docket);
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
		infoBuilder.license(swagger.getLicense());
		infoBuilder.licenseUrl(swagger.getLicenseUrl());
		docket.apiInfo(infoBuilder.build());
	}

	/**
	 * 类型映射配置
	 * @author Frodez
	 * @date 2019-12-04
	 */
	private void typeConfig(Docket docket) {
		//将模型重定向
		docket.directModelSubstitute(Result.class, SwaggerModel.class);
		docket.directModelSubstitute(LocalDate.class, String.class);
		docket.directModelSubstitute(LocalDateTime.class, String.class);
		docket.directModelSubstitute(LocalTime.class, String.class);
		TypeResolver resolver = new TypeResolver();
		//有扫描包之外的模型,则在此配置
		docket.additionalModels(resolver.resolve(QueryPage.class));
		docket.additionalModels(resolver.resolve(PageData.class));
		docket.additionalModels(resolver.resolve(SwaggerModel.class));
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
	 * 命名逻辑配置
	 * @author Frodez
	 * @date 2020-01-03
	 */
	private void genericTypeNamingStrategyConfig(Docket docket) {
		ReflectUtil.trySet(Docket.class, "genericsNamingStrategy", docket, new GenericTypeNamingStrategy() {

			private static final String OPEN = "<";

			private static final String CLOSE = ">";

			private static final String DELIM = ",";

			@Override
			public String getOpenGeneric() {
				return OPEN;
			}

			@Override
			public String getCloseGeneric() {
				return CLOSE;
			}

			@Override
			public String getTypeListDelimiter() {
				return DELIM;
			}
		});
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
			String message = SwaggerUtil.statusDescription(entry.getValue());
			ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder();
			messageBuilder.code(entry.getKey().value());
			messageBuilder.message(message);
			messageBuilder.responseModel(new ModelRef(SwaggerModel.class.getSimpleName()));
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
		SecurityContextBuilder builder = SecurityContext.builder();
		builder.securityReferences(defaultAuth());
		builder.forPaths(PathSelectors.regex(PropertyUtil.get(PropertyKey.Web.BASE_PATH)));
		return List.of(builder.build());
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
