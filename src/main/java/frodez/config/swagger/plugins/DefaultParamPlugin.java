package frodez.config.swagger.plugins;

import static com.google.common.base.Strings.emptyToNull;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER;

import com.google.common.collect.ArrayListMultimap;
import frodez.config.swagger.SwaggerProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
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
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 100)
public class DefaultParamPlugin implements ParameterBuilderPlugin {

	private final DescriptionResolver descriptions;

	private boolean useCustomerizedPluggins = false;

	@Autowired
	public DefaultParamPlugin(DescriptionResolver descriptions, EnumTypeDeterminer enumTypeDeterminer,
		SwaggerProperties properties) {
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
		ApiModel apiModel = methodParameter.getParameterType().getErasedType().getAnnotation(ApiModel.class);
		if (apiModel != null && !methodParameter.hasParameterAnnotation(ApiParam.class)) {
			defaultApiParam(context, apiModel);
		}
	}

	private void defaultApiParam(ParameterContext context, ApiModel annotation) {
		context.parameterBuilder().name(emptyToNull(annotation.description())).description(emptyToNull(descriptions
			.resolve(annotation.description()))).parameterAccess(emptyToNull(null)).defaultValue(emptyToNull(null))
			.allowMultiple(false).allowEmptyValue(false).required(true).scalarExample(new Example("")).complexExamples(
				ArrayListMultimap.create()).hidden(false).collectionFormat("").order(SWAGGER_PLUGIN_ORDER);
	}

}
