package frodez.util.renderer.reverter;

import frodez.util.common.StrUtil;
import frodez.util.io.FileUtil;
import frodez.util.renderer.FreemarkerRender;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

/**
 * 外联css转换器<br>
 * 这里没有实现js的外联到内联的转化,因为本转换器的使用场景不是http,而只是内部的转换,不需要执行js。<br>
 * @author Frodez
 * @date 2019-03-21
 */
@Slf4j
public class CSSReverter implements Reverter {

	/**
	 * 将html中外联的css变成内联,并去掉外联样式
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Override
	public String revert(String html) {
		Assert.notNull(html, "html must not be null");
		try {
			Document document = Jsoup.parse(html);
			Elements links = document.select("link[href]");
			Elements htmlElement = document.select("html");
			for (Element iter : links) {
				String path = iter.attr("href");
				if (!path.endsWith(".css")) {
					continue;
				}
				htmlElement.prepend(StrUtil.concat("<style type=\"text/css\">", FileUtil.readString(ResourceUtils
					.getFile(StrUtil.concat(FreemarkerRender.getLoaderPath(), path))), "</style>"));
			}
			links.remove();
			return document.html();
		} catch (Exception e) {
			log.error("[frodez.util.renderer.reverter.CSSReverter.revert]", e);
			return html;
		}
	}

}
