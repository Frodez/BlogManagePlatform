package frodez.config.swagger.plugin;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;
import frodez.config.swagger.SwaggerProperties;
import io.swagger.annotations.ApiModelProperty;
import java.lang.reflect.AnnotatedElement;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import springfox.documentation.schema.Annotations;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 必填默认值处理<br>
 * 只有存在声明可以为空的注解时,才会设定为非必填。<br>
 * @see javax.validation.constraints.Null
 * @see org.springframework.lang.Nullable
 * @see reactor.util.annotation.Nullable
 * @author Frodez
 * @date 2019-06-11
 */
@Component
@Profile({ "dev", "test" })
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 300)
public class DefaultRequiredPlugin implements ParameterBuilderPlugin, ModelPropertyBuilderPlugin {

	private boolean useCustomerizedPluggins = false;

	@Autowired
	public DefaultRequiredPlugin(SwaggerProperties properties) {
		this.useCustomerizedPluggins = properties.getUseCustomerizedPluggins();
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return useCustomerizedPluggins;
	}

	@Override
	public void apply(ParameterContext context) {
		context.parameterBuilder().required(isRequired(context.resolvedMethodParameter()));
	}

	/**
	 * 判断是否必填
	 * @author Frodez
	 * @date 2019-06-11
	 */
	private boolean isRequired(ResolvedMethodParameter methodParameter) {
		//优先判断spring的必填设置,如果spring未设置必填,则判断是否拥有可以为空的注解
		return requestXXX(methodParameter) ? true : !hasNullableAnnotation(methodParameter);
	}

	/**
	 * 判断是否拥有可以为空的注解
	 * @author Frodez
	 * @date 2019-06-11
	 */
	private boolean hasNullableAnnotation(ResolvedMethodParameter methodParameter) {
		return methodParameter.hasParameterAnnotation(Null.class) || methodParameter.hasParameterAnnotation(Nullable.class) || methodParameter
			.hasParameterAnnotation(reactor.util.annotation.Nullable.class);
	}

	/**
	 * 获取spring系列注解中的必填设置
	 * @author Frodez
	 * @date 2019-06-11
	 */
	private boolean requestXXX(ResolvedMethodParameter methodParameter) {
		Optional<RequestAttribute> requestAttribute = methodParameter.findAnnotation(RequestAttribute.class);
		if (requestAttribute.isPresent()) {
			return requestAttribute.get().required();
		}
		Optional<RequestBody> requestBody = methodParameter.findAnnotation(RequestBody.class);
		if (requestBody.isPresent()) {
			return requestBody.get().required();
		}
		Optional<RequestHeader> requestHeader = methodParameter.findAnnotation(RequestHeader.class);
		if (requestHeader.isPresent()) {
			return requestHeader.get().required();
		}
		Optional<RequestParam> requestParam = methodParameter.findAnnotation(RequestParam.class);
		if (requestParam.isPresent()) {
			return requestParam.get().required();
		}
		Optional<RequestPart> requestPart = methodParameter.findAnnotation(RequestPart.class);
		if (requestPart.isPresent()) {
			return requestPart.get().required();
		}
		Optional<PathVariable> pathVariable = methodParameter.findAnnotation(PathVariable.class);
		if (pathVariable.isPresent()) {
			return pathVariable.get().required();
		}
		return false;
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
		} else if (AnnotationUtils.findAnnotation(annotated, NotBlank.class) != null) {
			context.getBuilder().required(true);
		} else {
			ApiModelProperty annotation = AnnotationUtils.findAnnotation(annotated, ApiModelProperty.class);
			if (annotation != null && annotation.required()) {
				//如果ApiModelProperty上强制要求required为true,则为true
				context.getBuilder().required(true);
			} else {
				context.getBuilder().required(false);
			}
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
		} else if (Annotations.findPropertyAnnotation(beanPropertyDefinition, NotBlank.class).isPresent()) {
			context.getBuilder().required(true);
		} else {
			context.getBuilder().required(false);
		}
	}

}
