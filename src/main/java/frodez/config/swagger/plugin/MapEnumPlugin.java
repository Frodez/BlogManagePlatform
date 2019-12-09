package frodez.config.swagger.plugin;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.config.swagger.SwaggerProperties;
import frodez.constant.annotations.info.Description;
import frodez.constant.settings.DefEnum;
import frodez.constant.settings.DefStr;
import frodez.util.reflect.ReflectUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.Annotations;
import springfox.documentation.schema.Example;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
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
public class MapEnumPlugin implements ModelPropertyBuilderPlugin, ParameterBuilderPlugin {

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
	public void apply(ModelPropertyContext context) {
		resolveAnnotatedElement(context);
		resolveBeanPropertyDefinition(context);
	}

	@SneakyThrows
	private void resolveAnnotatedElement(ModelPropertyContext context) {
		AnnotatedElement annotated = context.getAnnotatedElement().orNull();
		if (annotated == null) {
			return;
		}
		if (AnnotationUtils.findAnnotation(annotated, ApiModelProperty.class) != null) {
			return;
		}
		MapEnum legalEnum = AnnotationUtils.findAnnotation(annotated, MapEnum.class);
		if (legalEnum != null) {
			String descs = getDescs(legalEnum.value(), legalEnum.descMethod());
			Object defaultValue = getDefaultValue(legalEnum.value());
			ModelPropertyBuilder builder = context.getBuilder();
			builder.description(descs);
			builder.example(defaultValue);
			builder.allowableValues(getAllowableValues(legalEnum.value()));
			builder.defaultValue(defaultValue == null ? DefStr.EMPTY : defaultValue.toString());
		}
	}

	@SneakyThrows
	private void resolveBeanPropertyDefinition(ModelPropertyContext context) {
		BeanPropertyDefinition beanPropertyDefinition = context.getBeanPropertyDefinition().orNull();
		if (beanPropertyDefinition == null) {
			return;
		}
		if (Annotations.findPropertyAnnotation(beanPropertyDefinition, ApiModelProperty.class).isPresent()) {
			return;
		}
		MapEnum legalEnum = Annotations.findPropertyAnnotation(beanPropertyDefinition, MapEnum.class).orNull();
		if (legalEnum != null) {
			String descs = getDescs(legalEnum.value(), legalEnum.descMethod());
			Object defaultValue = getDefaultValue(legalEnum.value());
			ModelPropertyBuilder builder = context.getBuilder();
			builder.description(descs);
			builder.example(defaultValue);
			builder.allowableValues(getAllowableValues(legalEnum.value()));
			builder.defaultValue(defaultValue == null ? DefStr.EMPTY : defaultValue.toString());
		}
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
			String descs = getDescs(enumParam.value(), enumParam.descMethod());
			Object defaultValue = getDefaultValue(enumParam.value());
			ParameterBuilder builder = context.parameterBuilder();
			builder.description(descs);
			builder.scalarExample(new Example(defaultValue == null ? DefStr.EMPTY : defaultValue.toString()));
			builder.allowableValues(getAllowableValues(enumParam.value()));
			builder.defaultValue(defaultValue == null ? DefStr.EMPTY : defaultValue.toString());
		}
	}

	@SneakyThrows
	private String getDescs(Class<?> klass, String descMethod) {
		FastMethod method = ReflectUtil.getFastMethod(klass, descMethod);
		String descs = method.invoke(null, ReflectUtil.EMPTY_ARRAY_OBJECTS).toString();
		String result = String.join(" ", getName(klass), descs);
		return result;
	}

	private String getName(Class<?> klass) {
		Description description = klass.getAnnotation(Description.class);
		return description == null ? DefStr.EMPTY : description.name();
	}

	@SneakyThrows
	private AllowableListValues getAllowableValues(Class<?> klass) {
		FastMethod method = ReflectUtil.getFastMethod(klass, DefEnum.VALS_METHOD_NAME);
		List<?> object = (List<?>) method.invoke(null, ReflectUtil.EMPTY_ARRAY_OBJECTS);
		return new AllowableListValues(object.stream().map((item) -> item.toString()).collect(Collectors.toList()), object.get(0).getClass()
			.getSimpleName());
	}

	@SneakyThrows
	private Object getDefaultValue(Class<?> klass) {
		FastMethod method = ReflectUtil.getFastMethod(klass, DefEnum.DEFAULT_VALUE_METHOD_NAME);
		return method.invoke(null, ReflectUtil.EMPTY_ARRAY_OBJECTS);
	}

}
