package frodez.config.swagger.plugin;

import static springfox.documentation.schema.ResolvedTypes.modelRefFactory;
import static springfox.documentation.spi.schema.contexts.ModelContext.returnValue;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import frodez.config.swagger.SwaggerProperties;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.constant.settings.DefDesc;
import frodez.util.beans.result.PageData;
import frodez.util.beans.result.Result;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponses;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
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
		messageBuilder.responseModel(new ModelRef(SwaggerModel.class.getSimpleName()));
		okResponses.add(messageBuilder.build());
		okResult = typeResolver.resolve(SwaggerModel.class);
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
		messageBuilder.message(okMessage);
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

	private <T> Optional<ResolvedType> resolvedType(TypeResolver resolver, Class<T> response, Container containerType) {
		if (Void.class != response && void.class != response) {
			if (containerType == Container.PAGE) {
				ResolvedType type = resolver.resolve(SwaggerModel.class, resolver.resolve(PageData.class, response));
				return Optional.of(type);
			} else if (containerType == Container.LIST) {
				ResolvedType type = resolver.resolve(SwaggerModel.class, resolver.resolve(List.class, response));
				return Optional.of(type);
			} else if (containerType == Container.SET) {
				ResolvedType type = resolver.resolve(SwaggerModel.class, resolver.resolve(Set.class, response));
				return Optional.of(type);
			} else {
				ResolvedType type = resolver.resolve(SwaggerModel.class, response);
				return Optional.of(type);
			}
		}
		return Optional.absent();
	}

	/**
	 * 用于显示的返回值模型,为frodez.util.beans.result.Result的泛型版本
	 * @see frodez.util.beans.result.Result
	 * @author Frodez
	 * @date 2019-12-05
	 */
	@Data
	@ApiModel(description = DefDesc.Message.RESULT)
	public static class SwaggerModel<T> implements Serializable {

		private static final long serialVersionUID = 1L;

		@ApiModelProperty(value = "状态", example = "1000")
		private int code;

		@ApiModelProperty(value = "消息", example = "成功")
		private String message;

		@ApiModelProperty(value = "数据")
		private T data;

	}

}
