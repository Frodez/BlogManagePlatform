package frodez.config.swagger.plugin;

import static com.google.common.base.Strings.emptyToNull;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER;

import frodez.config.swagger.SwaggerProperties;
import frodez.util.common.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
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

	private static String ApiParam = ApiParam.class.getCanonicalName();

	private static String ApiModel = ApiModel.class.getCanonicalName();

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
		log.warn(StrUtil.concat(pattern, "的参数", parameterName, "既未配置@", ApiParam, "注解,也未配置@", ApiModel, "注解"));
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

}
