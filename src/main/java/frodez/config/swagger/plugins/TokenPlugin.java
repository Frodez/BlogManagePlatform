package frodez.config.swagger.plugins;

import frodez.config.security.settings.SecurityProperties;
import frodez.config.security.util.Matcher;
import frodez.util.common.StrUtil;
import java.util.Arrays;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 自动添加token作为header在swagger上显示
 * @author Frodez
 * @date 2019-06-06
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class TokenPlugin implements OperationBuilderPlugin {

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public boolean supports(DocumentationType delimiter) {
		return true;
	}

	@Override
	public void apply(OperationContext context) {
		String basePath = context.getDocumentationContext().getPathMapping().or("");
		String path = StrUtil.concat(basePath, context.requestMappingPattern());
		if (Matcher.needVerify(path)) {
			context.operationBuilder().parameters(Arrays.asList(addTokenHeader()));
		}
	}

	private Parameter addTokenHeader() {
		return new ParameterBuilder().name("token").description(StrUtil.concat("key: ", securityProperties.getJwt()
			.getHeader(), "\nvalue: token", "\nprefix: ", securityProperties.getJwt().getTokenPrefix(), "..."))
			.required(true).parameterType("header").modelRef(new ModelRef("string")).build();
	}

}