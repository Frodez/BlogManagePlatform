package frodez.config.swagger.plugins;

import com.google.common.base.Optional;
import frodez.config.swagger.SwaggerProperties;
import frodez.util.common.EmptyUtil;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 自动将@RequestMapping中的name属性转换为接口名(用于方法)
 * @author Frodez
 * @date 2019-06-09
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 200)
public class DefaultApiOperationPlugin implements OperationBuilderPlugin {

	private final DescriptionResolver descriptions;

	private boolean useCustomerizedPluggins = false;

	@Autowired
	public DefaultApiOperationPlugin(DescriptionResolver descriptions, SwaggerProperties properties) {
		this.descriptions = descriptions;
		this.useCustomerizedPluggins = properties.getUseCustomerizedPluggins();
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return useCustomerizedPluggins;
	}

	@Override
	public void apply(OperationContext context) {
		String defaultName = resolveNameAttribute(context);
		if (EmptyUtil.no(defaultName)) {
			context.operationBuilder().summary(descriptions.resolve(defaultName));
		}
	}

	private String resolveNameAttribute(OperationContext context) {
		Optional<GetMapping> getMapping = context.findAnnotation(GetMapping.class);
		if (getMapping.isPresent()) {
			return getMapping.get().name();
		}
		Optional<PostMapping> postMapping = context.findAnnotation(PostMapping.class);
		if (postMapping.isPresent()) {
			return postMapping.get().name();
		}
		Optional<DeleteMapping> deleteMapping = context.findAnnotation(DeleteMapping.class);
		if (deleteMapping.isPresent()) {
			return deleteMapping.get().name();
		}
		Optional<PutMapping> putMapping = context.findAnnotation(PutMapping.class);
		if (putMapping.isPresent()) {
			return putMapping.get().name();
		}
		Optional<RequestMapping> requestMapping = context.findAnnotation(RequestMapping.class);
		if (requestMapping.isPresent()) {
			return requestMapping.get().name();
		}
		return null;
	}

}
