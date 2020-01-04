package frodez.config.swagger.plugin;

import frodez.config.swagger.SwaggerProperties;
import frodez.util.common.StrUtil;
import frodez.util.reflect.ReflectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 自动将@RequestMapping中的属性转换为接口属性(用于方法)<br>
 * 本插件使用spring注解中一些不常用属性作为替代。若因此产生冲突,请禁用本插件。
 * @author Frodez
 * @date 2019-06-09
 */
@Slf4j
@Component
@Profile({ "dev", "test" })
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 300)
public class DefaultEndPointPlugin implements OperationBuilderPlugin {

	private final DescriptionResolver descriptions;

	private boolean useCustomerizedPluggins = false;

	@Autowired
	public DefaultEndPointPlugin(DescriptionResolver descriptions, SwaggerProperties properties) {
		this.descriptions = descriptions;
		this.useCustomerizedPluggins = properties.getUseCustomerizedPluggins();
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return useCustomerizedPluggins;
	}

	@Override
	public void apply(OperationContext context) {
		if (context.findAnnotation(ApiOperation.class).isPresent()) {
			return;
		}
		EndPointInfo info = resolveNameAttribute(context);
		if (info == null) {
			log.warn(StrUtil.concat(context.requestMappingPattern(), "的信息未配置"));
			return;
		}
		OperationBuilder builder = context.operationBuilder();
		builder.summary(descriptions.resolve(info.name));
		builder.tags(Set.of(info.controllerName));
		ReflectUtil.set(OperationBuilder.class, "consumes", builder, Set.of(info.consumes));
		ReflectUtil.set(OperationBuilder.class, "produces", builder, Set.of(info.produces));
	}

	private EndPointInfo resolveNameAttribute(OperationContext context) {
		EndPointInfo info = new EndPointInfo();
		info.controllerName = resolveApiName(context);
		boolean isRestEndPoint = isRestEndPoint(context);
		GetMapping getMapping = context.findAnnotation(GetMapping.class).orNull();
		if (getMapping != null) {
			info.name = getMapping.name();
			info.consumes = getMapping.consumes();
			info.produces = resolveJsonInfo(isRestEndPoint, getMapping.produces());
			return info;
		}
		PostMapping postMapping = context.findAnnotation(PostMapping.class).orNull();
		if (postMapping != null) {
			info.name = postMapping.name();
			info.consumes = postMapping.consumes();
			info.produces = resolveJsonInfo(isRestEndPoint, postMapping.produces());
			return info;
		}
		DeleteMapping deleteMapping = context.findAnnotation(DeleteMapping.class).orNull();
		if (deleteMapping != null) {
			info.name = deleteMapping.name();
			info.consumes = deleteMapping.consumes();
			info.produces = resolveJsonInfo(isRestEndPoint, deleteMapping.produces());
			return info;
		}
		PutMapping putMapping = context.findAnnotation(PutMapping.class).orNull();
		if (putMapping != null) {
			info.name = putMapping.name();
			info.consumes = putMapping.consumes();
			info.produces = resolveJsonInfo(isRestEndPoint, putMapping.produces());
			return info;
		}
		RequestMapping requestMapping = context.findAnnotation(RequestMapping.class).orNull();
		if (requestMapping != null) {
			info.name = requestMapping.name();
			info.consumes = requestMapping.consumes();
			info.produces = resolveJsonInfo(isRestEndPoint, requestMapping.produces());
			return info;
		}
		return null;
	}

	private String resolveApiName(OperationContext context) {
		Api api = context.findControllerAnnotation(Api.class).orNull();
		if (api != null) {
			return api.tags()[0];
		}
		GetMapping getMapping = context.findControllerAnnotation(GetMapping.class).orNull();
		if (getMapping != null) {
			return getMapping.name();
		}
		PostMapping postMapping = context.findControllerAnnotation(PostMapping.class).orNull();
		if (postMapping != null) {
			return postMapping.name();
		}
		DeleteMapping deleteMapping = context.findControllerAnnotation(DeleteMapping.class).orNull();
		if (deleteMapping != null) {
			return deleteMapping.name();
		}
		PutMapping putMapping = context.findControllerAnnotation(PutMapping.class).orNull();
		if (putMapping != null) {
			return putMapping.name();
		}
		RequestMapping requestMapping = context.findControllerAnnotation(RequestMapping.class).orNull();
		if (requestMapping != null) {
			return requestMapping.name();
		}
		return "";
	}

	private boolean isRestEndPoint(OperationContext context) {
		if (context.findControllerAnnotation(RestController.class).isPresent()) {
			return true;
		}
		if (context.findAnnotation(ResponseBody.class).isPresent()) {
			return true;
		}
		return false;
	}

	private String[] resolveJsonInfo(boolean isRestEndPoint, String[] strings) {
		if (!isRestEndPoint) {
			return strings;
		}
		boolean isAbsent = true;
		for (String string : strings) {
			if (string.equals(MediaType.APPLICATION_JSON_VALUE)) {
				isAbsent = false;
				break;
			}
		}
		if (isAbsent) {
			if (strings.length == 0) {
				return new String[] { MediaType.APPLICATION_JSON_VALUE };
			}
			String[] newStrings = new String[strings.length + 1];
			System.arraycopy(strings, 0, newStrings, 0, strings.length);
			newStrings[strings.length] = MediaType.APPLICATION_JSON_VALUE;
			return newStrings;
		} else {
			return strings;
		}
	}

	@Getter
	private static class EndPointInfo {

		public String controllerName;

		public String name;

		public String[] consumes;

		public String[] produces;

	}

}
