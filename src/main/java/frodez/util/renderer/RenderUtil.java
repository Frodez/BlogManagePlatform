package frodez.util.renderer;

import freemarker.template.Configuration;
import frodez.util.constant.setting.PropertyKey;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.PropertyUtil;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RenderUtil {

	private static Configuration configuration;

	private static String suffix;

	@PostConstruct
	private void init() {
		configuration = ContextUtil.get(Configuration.class);
		suffix = PropertyUtil.get(PropertyKey.FreeMarker.SUFFIX);
		Assert.notNull(configuration, "configuration must not be null");
		Assert.notNull(suffix, "suffix must not be null");
	}

	public static Configuration configuration() {
		return configuration;
	}

	/**
	 * 渲染页面,并转变为String
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static String render(String templateName) {
		return render(templateName, new HashMap<>());
	}

	/**
	 * 渲染页面,并转变为String
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static String render(String templateName, Map<String, Object> params) {
		Assert.notNull(templateName, "templateName must not be null");
		Assert.notNull(params, "params must not be null");
		try {
			StringWriter writer = new StringWriter();
			configuration.getTemplate(templateName.concat(suffix)).process(params, writer);
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 渲染页面,并转变为String<br>
	 * 注意:本方法不会自动关闭流。
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static void render(String templateName, Writer writer) {
		render(templateName, new HashMap<>(), writer, false);
	}

	/**
	 * 渲染页面,并转变为String<br>
	 * 注意:本方法不会自动关闭流。
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static void render(String templateName, Map<String, Object> params, Writer writer) {
		render(templateName, params, writer, false);
	}

	/**
	 * 渲染页面,并转变为String<br>
	 * autoClose为true时,自动关闭流;为false时,不自动关闭流。
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static void render(String templateName, Writer writer, boolean autoClose) {
		render(templateName, new HashMap<>(), writer, autoClose);
	}

	/**
	 * 渲染页面,并转变为String<br>
	 * autoClose为true时,自动关闭流;为false时,不自动关闭流。
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static void render(String templateName, Map<String, Object> params, Writer writer, boolean autoClose) {
		Assert.notNull(templateName, "templateName must not be null");
		Assert.notNull(params, "params must not be null");
		Assert.notNull(writer, "writer must not be null");
		try {
			configuration.getTemplate(templateName.concat(suffix)).process(params, writer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (autoClose) {
				try {
					writer.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

}
