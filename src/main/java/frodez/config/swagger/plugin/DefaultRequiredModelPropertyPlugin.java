package frodez.config.swagger.plugin;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import frodez.config.swagger.SwaggerProperties;
import java.lang.reflect.AnnotatedElement;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.Annotations;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 自动检测字段是否为必填
 * @author Frodez
 * @date 2019-06-11
 */
@Component
@Profile({ "dev", "test" })
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 300)
public class DefaultRequiredModelPropertyPlugin implements ModelPropertyBuilderPlugin {

	private boolean useCustomerizedPluggins = false;

	@Autowired
	public DefaultRequiredModelPropertyPlugin(SwaggerProperties properties) {
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

	private void resolveAnnotatedElement(ModelPropertyContext context) {
		AnnotatedElement annotated = context.getAnnotatedElement().orNull();
		if (annotated == null) {
			return;
		}
		if (AnnotationUtils.findAnnotation(annotated, NotNull.class) != null) {
			context.getBuilder().required(true);
		} else if (AnnotationUtils.findAnnotation(annotated, NotEmpty.class) != null) {
			context.getBuilder().required(true);
		} else {
			context.getBuilder().required(false);
		}
	}

	private void resolveBeanPropertyDefinition(ModelPropertyContext context) {
		BeanPropertyDefinition beanPropertyDefinition = context.getBeanPropertyDefinition().orNull();
		if (beanPropertyDefinition == null) {
			return;
		}
		if (Annotations.findPropertyAnnotation(beanPropertyDefinition, NotNull.class).isPresent()) {
			context.getBuilder().required(true);
		} else if (Annotations.findPropertyAnnotation(beanPropertyDefinition, NotEmpty.class).isPresent()) {
			context.getBuilder().required(true);
		} else {
			context.getBuilder().required(false);
		}
	}

}
