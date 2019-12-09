package frodez.config.swagger.plugin;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import com.google.common.base.Optional;
import frodez.config.swagger.SwaggerProperties;
import frodez.util.common.StrUtil;
import frodez.util.reflect.ReflectUtil;
import io.swagger.annotations.Api;
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
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.contexts.ApiListingContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 自动将@RequestMapping中的属性转换为接口属性(用于类)<br>
 * 本插件使用spring注解中一些不常用属性作为替代。若因此产生冲突,请禁用本插件。
 * @author Frodez
 * @date 2019-12-08
 */
@Slf4j
@Component
@Profile({ "dev", "test" })
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 300)
public class DefaultApiNamePlugin implements ApiListingBuilderPlugin {

	private boolean useCustomerizedPluggins = false;

	@Autowired
	public DefaultApiNamePlugin(SwaggerProperties properties) {
		this.useCustomerizedPluggins = properties.getUseCustomerizedPluggins();
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return useCustomerizedPluggins;
	}

	@Override
	public void apply(ApiListingContext context) {
		Optional<? extends Class<?>> controller = context.getResourceGroup().getControllerClass();
		if (controller.isPresent()) {
			resolveController(context, controller.get());
		}
	}

	private void resolveController(ApiListingContext context, Class<?> controller) {
		if (findAnnotation(controller, Api.class) != null) {
			return;
		}
		ControllerInfo info = resolveNameAttribute(controller);
		if (info == null) {
			log.warn(StrUtil.concat(controller.getName(), "的信息未配置"));
		}
		ApiListingBuilder builder = context.apiListingBuilder();
		ReflectUtil.trySet(ApiListingBuilder.class, "tagNames", builder, Set.of(info.name));
		builder.consumes(Set.of(info.consumes));
		builder.produces(Set.of(info.produces));
	}

	private ControllerInfo resolveNameAttribute(Class<?> controller) {
		ControllerInfo info = new ControllerInfo();
		boolean isRestController = isRestController(controller);
		GetMapping getMapping = findAnnotation(controller, GetMapping.class);
		if (getMapping != null) {
			info.name = getMapping.name();
			info.consumes = getMapping.consumes();
			info.produces = resolveJsonInfo(isRestController, getMapping.produces());
			return info;
		}
		PostMapping postMapping = findAnnotation(controller, PostMapping.class);
		if (postMapping != null) {
			info.name = postMapping.name();
			info.consumes = postMapping.consumes();
			info.produces = resolveJsonInfo(isRestController, postMapping.produces());
			return info;
		}
		DeleteMapping deleteMapping = findAnnotation(controller, DeleteMapping.class);
		if (deleteMapping != null) {
			info.name = deleteMapping.name();
			info.consumes = deleteMapping.consumes();
			info.produces = resolveJsonInfo(isRestController, deleteMapping.produces());
			return info;
		}
		PutMapping putMapping = findAnnotation(controller, PutMapping.class);
		if (putMapping != null) {
			info.name = putMapping.name();
			info.consumes = putMapping.consumes();
			info.produces = resolveJsonInfo(isRestController, putMapping.produces());
			return info;
		}
		RequestMapping requestMapping = findAnnotation(controller, RequestMapping.class);
		if (requestMapping != null) {
			info.name = requestMapping.name();
			info.consumes = requestMapping.consumes();
			info.produces = resolveJsonInfo(isRestController, requestMapping.produces());
			return info;
		}
		return null;
	}

	private boolean isRestController(Class<?> controller) {
		return findAnnotation(controller, RestController.class) != null || findAnnotation(controller, ResponseBody.class) != null;
	}

	private String[] resolveJsonInfo(boolean isRestController, String[] strings) {
		if (!isRestController) {
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
	private static class ControllerInfo {

		public String name;

		public String[] consumes;

		public String[] produces;

	}

}
