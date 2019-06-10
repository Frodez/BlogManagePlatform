package frodez.config.swagger.plugins;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;
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
		Optional<AnnotatedElement> annotated = context.getAnnotatedElement();
		if (annotated.isPresent()) {
			if (AnnotationUtils.findAnnotation(annotated.get(), NotNull.class) != null) {
				context.getBuilder().required(true);
			} else if (AnnotationUtils.findAnnotation(annotated.get(), NotEmpty.class) != null) {
				context.getBuilder().required(true);
			} else if (AnnotationUtils.findAnnotation(annotated.get(), NotEmpty.class) != null) {
				context.getBuilder().required(true);
			}
		}
	}

	private void resolveBeanPropertyDefinition(ModelPropertyContext context) {
		Optional<BeanPropertyDefinition> beanPropertyDefinition = context.getBeanPropertyDefinition();
		if (beanPropertyDefinition.isPresent()) {
			if (Annotations.findPropertyAnnotation(beanPropertyDefinition.get(), NotNull.class) != null) {
				context.getBuilder().required(true);
			} else if (Annotations.findPropertyAnnotation(beanPropertyDefinition.get(), NotEmpty.class) != null) {
				context.getBuilder().required(true);
			} else if (Annotations.findPropertyAnnotation(beanPropertyDefinition.get(), NotEmpty.class) != null) {
				context.getBuilder().required(true);
			}
		}
	}

}
