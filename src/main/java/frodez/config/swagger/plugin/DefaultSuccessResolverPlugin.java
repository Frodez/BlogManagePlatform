package frodez.config.swagger.plugin;

import static springfox.documentation.schema.ResolvedTypes.modelRefFactory;
import static springfox.documentation.spi.schema.contexts.ModelContext.returnValue;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import frodez.config.swagger.SwaggerProperties;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.ContainerType;
import frodez.util.beans.result.PageData;
import frodez.util.beans.result.Result;
import io.swagger.annotations.ApiResponses;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 用自定义注解描述成功返回值和返回模型
 * @author Frodez
 * @date 2019-12-05
 */
@Component
@Profile({ "dev", "test" })
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 200)
public class DefaultSuccessResolverPlugin implements OperationBuilderPlugin, OperationModelsProviderPlugin {

	private final TypeNameExtractor typeNameExtractor;

	private final TypeResolver typeResolver;

	private boolean useCustomerizedPluggins = false;

	private String okMessage;

	private Set<ResponseMessage> okResponses = new HashSet<>();

	private ResolvedType okResult;

	public DefaultSuccessResolverPlugin(TypeNameExtractor typeNameExtractor, TypeResolver typeResolver, SwaggerProperties properties) {
		this.typeNameExtractor = typeNameExtractor;
		this.typeResolver = typeResolver;
		this.useCustomerizedPluggins = properties.getUseCustomerizedPluggins();
		okMessage = String.join(" | ", Stream.of(Result.ResultEnum.values()).filter((item) -> {
			return item.getStatus() == HttpStatus.OK;
		}).map((iter) -> {
			return iter.getDesc() + ",自定义状态码:" + iter.getVal();
		}).collect(Collectors.toList()));
		ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder();
		messageBuilder.code(HttpStatus.OK.value());
		messageBuilder.message(okMessage);
		messageBuilder.responseModel(new ModelRef(Result.class.getSimpleName()));
		okResponses.add(messageBuilder.build());
		okResult = typeResolver.resolve(Result.class);
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return useCustomerizedPluggins;
	}

	@Override
	public void apply(OperationContext context) {
		//TODO:这样做屏蔽了@ResponseHeader的功能,因为没有了该注解。
		//由于本项目header为统一配置,故未实现替代功能。有必要的话需要提供一个替代实现。
		if (context.findAnnotation(ApiResponses.class).isPresent()) {
			return;
		}
		Optional<Success> annotation = context.findAnnotation(Success.class);
		if (!annotation.isPresent()) {
			context.operationBuilder().responseMessages(okResponses);
			return;
		}
		Success success = annotation.get();
		ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder();
		messageBuilder.code(HttpStatus.OK.value());
		messageBuilder.message(okMessage.concat("\n注意:本返回值仍然包装在通用Result内,位于Result.data位置"));
		messageBuilder.responseModel(resolveModel(context, success).orNull());
		context.operationBuilder().responseMessages(Set.of(messageBuilder.build()));
	}

	@Override
	public void apply(RequestMappingContext context) {
		if (context.findAnnotation(ApiResponses.class).isPresent()) {
			return;
		}
		Optional<Success> annotation = context.findAnnotation(Success.class);
		if (!annotation.isPresent()) {
			context.operationModelsBuilder().addReturn(okResult);
			return;
		}
		Success success = annotation.get();
		context.operationModelsBuilder().addReturn(resolvedType(typeResolver, success.value(), success.containerType()).orNull());
	}

	private Optional<ModelReference> resolveModel(OperationContext context, Success success) {
		ModelContext modelContext = returnValue(context.getGroupName(), success.value(), context.getDocumentationType(), context
			.getAlternateTypeProvider(), context.getGenericsNamingStrategy(), context.getIgnorableParameterTypes());
		Optional<ResolvedType> type = resolvedType(typeResolver, success.value(), success.containerType());
		return Optional.of(modelRefFactory(modelContext, typeNameExtractor).apply(context.alternateFor(type.get())));
	}

	private static Optional<ResolvedType> resolvedType(TypeResolver resolver, Class<?> response, ContainerType containerType) {
		if (Void.class != response && void.class != response) {
			if (containerType == ContainerType.PAGE) {
				return Optional.of(resolver.resolve(PageData.class, response));
			} else if (containerType == ContainerType.LIST) {
				return Optional.of(resolver.resolve(List.class, response));
			} else if (containerType == ContainerType.SET) {
				return Optional.of(resolver.resolve(Set.class, response));
			} else {
				return Optional.of(resolver.resolve(response));
			}
		}
		return Optional.absent();
	}

}
