package frodez.util.renderer;

import freemarker.template.Configuration;
import frodez.util.common.EmptyUtil;
import frodez.util.reflect.ReflectUtil;
import frodez.util.renderer.reverter.Reverter;
import frodez.util.spring.ContextUtil;
import java.io.StringWriter;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 模板引擎渲染工具类<br>
 * <strong>警告!!!如果要使用本类的方法,必须确保RenderUtil已经被初始化!</strong><br>
 * <strong>方式:在使用本方法的类上加入@DependsOn("renderUtil")注解。</strong>
 * @author Frodez
 * @date 2019-03-27
 */
@Lazy
@Component("renderUtil")
public class RenderUtil {

	private static Configuration configuration;

	@Getter
	private static String loaderPath;

	@Getter
	private static String suffix;

	private static Map<RenderMode, Reverter> reverterMap = new EnumMap<>(RenderMode.class);

	@PostConstruct
	private void init() {
		configuration = ContextUtil.get(Configuration.class);
		FreeMarkerProperties properties = ContextUtil.get(FreeMarkerProperties.class);
		loaderPath = properties.getTemplateLoaderPath()[0];
		suffix = properties.getSuffix();
		for (RenderMode mode : RenderMode.values()) {
			reverterMap.put(mode, ReflectUtil.newInstance(mode.getReverter()));
		}
		Assert.notNull(configuration, "configuration must not be null");
		Assert.notNull(loaderPath, "loaderPath must not be null");
		Assert.notNull(suffix, "suffix must not be null");
	}

	private static String revert(String html, RenderMode... modes) {
		if (EmptyUtil.no(modes)) {
			for (RenderMode mode : modes) {
				html = reverterMap.get(mode).revert(html);
			}
		}
		return html;
	}

	public static Configuration configuration() {
		return configuration;
	}

	/**
	 * 渲染页面,并转变为String(默认内联css)
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static String render(String templateName) {
		return render(templateName, new HashMap<>(), RenderMode.CSSREVERTER);
	}

	/**
	 * 渲染页面,并转变为String
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static String render(String templateName, RenderMode... modes) {
		return render(templateName, new HashMap<>(), modes);
	}

	/**
	 * 渲染页面,并转变为String
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static String render(String templateName, Map<String, Object> params, RenderMode... modes) {
		Assert.notNull(templateName, "templateName must not be null");
		Assert.notNull(params, "params must not be null");
		try {
			StringWriter writer = new StringWriter();
			configuration.getTemplate(templateName.concat(suffix)).process(params, writer);
			return revert(writer.toString(), modes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}