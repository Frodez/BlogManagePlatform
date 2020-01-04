package frodez.util.renderer;

import freemarker.template.Configuration;
import frodez.util.common.EmptyUtil;
import frodez.util.common.StrUtil;
import frodez.util.reflect.ReflectUtil;
import frodez.util.renderer.reverter.Reverter;
import frodez.util.spring.ContextUtil;
import java.io.StringWriter;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 模板引擎渲染工具类<br>
 * @author Frodez
 * @date 2019-03-27
 */
@Component("FreemarkerRender")
public class FreemarkerRender {

	private static Configuration configuration;

	@Getter
	private static String loaderPath;

	@Getter
	private static String suffix;

	private static Map<RenderMode, Reverter> reverterMap = new EnumMap<>(RenderMode.class);

	@PostConstruct
	private void init() {
		configuration = ContextUtil.bean(Configuration.class);
		FreeMarkerProperties properties = ContextUtil.bean(FreeMarkerProperties.class);
		loaderPath = properties.getTemplateLoaderPath()[0];
		suffix = properties.getSuffix();
		for (RenderMode mode : RenderMode.values()) {
			reverterMap.put(mode, ReflectUtil.instance(mode.getReverter()));
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

	/**
	 * 获取freemarker的执行引擎
	 * @author Frodez
	 * @date 2019-04-12
	 */
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
	 * 渲染页面,并转变为String。可选择添加不同的渲染模式。<br>
	 * 默认无任何需要参数。
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static String render(String templateName, RenderMode... modes) {
		return render(templateName, new HashMap<>(), modes);
	}

	/**
	 * 渲染页面,并转变为String。可选择添加不同的渲染模式。
	 * @param params freemark页面可能需要的参数
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@SneakyThrows
	public static String render(String templateName, Map<String, Object> params, RenderMode... modes) {
		Assert.notNull(templateName, "templateName must not be null");
		Assert.notNull(params, "params must not be null");
		StringWriter writer = new StringWriter();
		configuration.getTemplate(StrUtil.concat(templateName, suffix)).process(params, writer);
		return revert(writer.toString(), modes);
	}

}
