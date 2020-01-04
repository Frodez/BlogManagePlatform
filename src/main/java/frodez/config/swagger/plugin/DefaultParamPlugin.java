package frodez.config.swagger.plugin;

import static springfox.documentation.schema.Collections.collectionElementType;
import static springfox.documentation.schema.Collections.isContainerType;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER;

import com.fasterxml.classmate.ResolvedType;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.config.aop.validation.annotation.common.MapEnum.MapEnumHelper;
import frodez.config.swagger.SwaggerProperties;
import frodez.constant.settings.DefStr;
import frodez.util.common.StrUtil;
import frodez.util.reflect.TypeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
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
import springfox.documentation.schema.Example;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
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
		resolveParameterType(context);
		if (context.resolvedMethodParameter().hasParameterAnnotation(ApiParam.class)) {
			return;
		}
		resolveMapEnum(context);
		resolveApiParam(context);
	}

	private void resolveParameterType(ParameterContext context) {
		ResolvedMethodParameter parameter = context.resolvedMethodParameter();
		ResolvedType type = context.alternateFor(parameter.getParameterType());
		//Multi-part file trumps any other annotations
		if (isFileType(type) || isListOfFiles(type)) {
			context.parameterBuilder().parameterType("form");
			return;
		}
		if (parameter.hasParameterAnnotation(PathVariable.class)) {
			context.parameterBuilder().parameterType("path");
			return;
		}
		if (parameter.hasParameterAnnotation(RequestBody.class)) {
			context.parameterBuilder().parameterType("body");
			return;
		}
		if (parameter.hasParameterAnnotation(RequestPart.class)) {
			context.parameterBuilder().parameterType("formData");
			return;
		}
		if (parameter.hasParameterAnnotation(RequestParam.class)) {
			Set<? extends MediaType> consumes = context.getOperationContext().consumes();
			HttpMethod method = context.getOperationContext().httpMethod();
			if (consumes.contains(MediaType.APPLICATION_FORM_URLENCODED) && method == HttpMethod.POST) {
				context.parameterBuilder().parameterType("form");
			} else if (consumes.contains(MediaType.MULTIPART_FORM_DATA) && method == HttpMethod.POST) {
				context.parameterBuilder().parameterType("formData");
			} else {
				context.parameterBuilder().parameterType("query");
			}
			return;
		}
		if (parameter.hasParameterAnnotation(RequestHeader.class)) {
			context.parameterBuilder().parameterType("header");
			return;
		}
		if (parameter.hasParameterAnnotation(ModelAttribute.class)) {
			log.warn(StrUtil.concat("@ModelAttribute annotated parameters should have already been expanded via ",
				"the ExpandedParameterBuilderPlugin"));
		}
		if (parameter.hasParameterAnnotation(ApiParam.class)) {
			context.parameterBuilder().parameterType("query");
			return;
		}
		if (parameter.hasParameterAnnotation(MapEnum.class)) {
			context.parameterBuilder().parameterType("query");
			return;
		}
		context.parameterBuilder().parameterType("body");
	}

	private boolean isListOfFiles(ResolvedType parameterType) {
		return isContainerType(parameterType) && isFileType(collectionElementType(parameterType));
	}

	private boolean isFileType(ResolvedType parameterType) {
		return MultipartFile.class.isAssignableFrom(parameterType.getErasedType());
	}

	private void resolveApiParam(ParameterContext context) {
		ResolvedType resolvedType = context.resolvedMethodParameter().getParameterType();
		Class<?> parameterClass = resolveParamType(resolvedType);
		if (parameterClass == null) {
			log.warn(StrUtil.concat(resolvedType.getBriefDescription(), "的类型无法被DefaultParamPlugin解析"));
			@SuppressWarnings("unused") int a = 1;
			return;
		}
		ApiModel apiModel = parameterClass.getAnnotation(ApiModel.class);
		if (apiModel == null) {
			if (!BeanUtils.isSimpleProperty(parameterClass)) {
				warn(context, parameterClass);
			}
			return;
		}
		ParameterBuilder builder = context.parameterBuilder();
		builder.name(apiModel.description());
		builder.description(descriptions.resolve(apiModel.description()));
		builder.allowMultiple(false);
		builder.allowEmptyValue(false);
		builder.hidden(false);
		builder.collectionFormat("");
		builder.order(SWAGGER_PLUGIN_ORDER);
	}

	private Class<?> resolveParamType(ResolvedType resolvedType) {
		if (TypeUtil.isSimpleType(resolvedType)) {
			return resolvedType.getErasedType();
		} else {
			List<ResolvedType> collectionResolvedTypes = TypeUtil.resolveGenericType(Collection.class, resolvedType);
			if (collectionResolvedTypes == null) {
				return null;
			} else {
				ResolvedType collectionResolvedType = collectionResolvedTypes.get(0);
				if (TypeUtil.isComplexType(collectionResolvedType)) {
					return null;
				} else {
					return collectionResolvedType.getErasedType();
				}
			}
		}
	}

	private static void warn(ParameterContext context, Class<?> parameterClass) {
		String pattern = context.getOperationContext().requestMappingPattern();
		String parameterName = parameterClass.getCanonicalName();
		log.warn(StrUtil.concat(pattern, "的参数", parameterName, "既未配置@", param, "注解,也未配置@", model, "注解"));
	}

	@SneakyThrows
	private void resolveMapEnum(ParameterContext context) {
		MapEnum enumParam = context.resolvedMethodParameter().findAnnotation(MapEnum.class).orNull();
		if (enumParam != null) {
			String descs = MapEnumHelper.getDescs(enumParam.value(), enumParam.descMethod());
			Object defaultValue = MapEnumHelper.getDefaultValue(enumParam.value());
			ParameterBuilder builder = context.parameterBuilder();
			builder.description(descs);
			builder.scalarExample(new Example(defaultValue == null ? DefStr.EMPTY : defaultValue.toString()));
			builder.allowableValues(MapEnumHelper.getAllowableValues(enumParam.value()));
			builder.defaultValue(defaultValue == null ? DefStr.EMPTY : defaultValue.toString());
		}
	}

}
