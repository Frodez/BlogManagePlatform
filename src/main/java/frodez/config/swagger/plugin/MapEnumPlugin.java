package frodez.config.swagger.plugin;

import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.config.aop.validation.annotation.common.MapEnum.MapEnumHelper;
import frodez.config.swagger.SwaggerProperties;
import frodez.constant.settings.DefStr;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.Example;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 枚举映射信息处理插件
 * @author Frodez
 * @date 2019-12-08
 */
@Component
@Profile({ "dev", "test" })
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 300)
public class MapEnumPlugin implements ParameterBuilderPlugin {

	private boolean useCustomerizedPluggins = false;

	@Autowired
	public MapEnumPlugin(SwaggerProperties properties) {
		this.useCustomerizedPluggins = properties.getUseCustomerizedPluggins();
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return useCustomerizedPluggins;
	}

	@Override
	public void apply(ParameterContext context) {
		if (context.resolvedMethodParameter().findAnnotation(ApiParam.class).isPresent()) {
			return;
		}
		resolveParameter(context);
	}

	@SneakyThrows
	private void resolveParameter(ParameterContext context) {
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
