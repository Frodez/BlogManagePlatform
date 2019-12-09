package frodez.config.swagger.plugin;

import static com.google.common.base.Strings.emptyToNull;
import static springfox.documentation.schema.Collections.collectionElementType;
import static springfox.documentation.schema.Collections.isContainerType;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER;

import com.fasterxml.classmate.ResolvedType;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.config.swagger.SwaggerProperties;
import frodez.util.common.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 自动将@ApiModel中的数据转换为默认参数描述
 * @author Frodez
 * @date 2019-06-06
 */
@Slf4j
@Component
@Profile({ "dev", "test" })
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 300)
public class DefaultParamPlugin implements ParameterBuilderPlugin {

	private static String param = ApiParam.class.getCanonicalName();

	private static String model = ApiModel.class.getCanonicalName();

	private final DescriptionResolver descriptions;

	private boolean useCustomerizedPluggins = false;

	@Autowired
	public DefaultParamPlugin(DescriptionResolver descriptions, EnumTypeDeterminer enumTypeDeterminer, SwaggerProperties properties) {
		this.descriptions = descriptions;
		this.useCustomerizedPluggins = properties.getUseCustomerizedPluggins();
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return useCustomerizedPluggins;
	}

	@Override
	public void apply(ParameterContext context) {
		context.parameterBuilder().parameterType(resolveParameterType(context));
		ResolvedMethodParameter methodParameter = context.resolvedMethodParameter();
		if (methodParameter.hasParameterAnnotation(ApiParam.class)) {
			return;
		}
		Class<?> parameterClass = methodParameter.getParameterType().getErasedType();
		ApiModel apiModel = parameterClass.getAnnotation(ApiModel.class);
		if (apiModel == null) {
			if (!BeanUtils.isSimpleProperty(parameterClass)) {
				warn(context, parameterClass);
			}
			return;
		}
		setDefaultApiParam(context, apiModel);
	}

	private static void warn(ParameterContext context, Class<?> parameterClass) {
		String pattern = context.getOperationContext().requestMappingPattern();
		String parameterName = parameterClass.getCanonicalName();
		log.warn(StrUtil.concat(pattern, "的参数", parameterName, "既未配置@", param, "注解,也未配置@", model, "注解"));
	}

	private void setDefaultApiParam(ParameterContext context, ApiModel annotation) {
		ParameterBuilder builder = context.parameterBuilder();
		builder.name(emptyToNull(annotation.description()));
		builder.description(emptyToNull(descriptions.resolve(annotation.description())));
		builder.parameterAccess(null);
		builder.allowMultiple(false);
		builder.allowEmptyValue(false);
		builder.hidden(false);
		builder.collectionFormat("");
		builder.order(SWAGGER_PLUGIN_ORDER);
	}

	private String resolveParameterType(ParameterContext context) {
		ResolvedMethodParameter parameter = context.resolvedMethodParameter();
		ResolvedType type = context.alternateFor(parameter.getParameterType());
		//Multi-part file trumps any other annotations
		if (isFileType(type) || isListOfFiles(type)) {
			return "form";
		}
		if (parameter.hasParameterAnnotation(PathVariable.class)) {
			return "path";
		}
		if (parameter.hasParameterAnnotation(RequestBody.class)) {
			return "body";
		}
		if (parameter.hasParameterAnnotation(RequestPart.class)) {
			return "formData";
		}
		if (parameter.hasParameterAnnotation(RequestParam.class)) {
			OperationContext operationContext = context.getOperationContext();
			return determineScalarParameterType(operationContext.consumes(), operationContext.httpMethod());
		}
		if (parameter.hasParameterAnnotation(RequestHeader.class)) {
			return "header";
		}
		if (parameter.hasParameterAnnotation(ModelAttribute.class)) {
			log.warn(StrUtil.concat("@ModelAttribute annotated parameters should have already been expanded via ",
				"the ExpandedParameterBuilderPlugin"));
		}
		if (parameter.hasParameterAnnotation(ApiParam.class)) {
			return "query";
		}
		if (parameter.hasParameterAnnotation(MapEnum.class)) {
			return "query";
		}
		return "body";
	}

	private boolean isListOfFiles(ResolvedType parameterType) {
		return isContainerType(parameterType) && isFileType(collectionElementType(parameterType));
	}

	private boolean isFileType(ResolvedType parameterType) {
		return MultipartFile.class.isAssignableFrom(parameterType.getErasedType());
	}

	private String determineScalarParameterType(Set<? extends MediaType> consumes, HttpMethod method) {
		String parameterType = "query";
		if (consumes.contains(MediaType.APPLICATION_FORM_URLENCODED) && method == HttpMethod.POST) {
			parameterType = "form";
		} else if (consumes.contains(MediaType.MULTIPART_FORM_DATA) && method == HttpMethod.POST) {
			parameterType = "formData";
		}
		return parameterType;
	}

}
