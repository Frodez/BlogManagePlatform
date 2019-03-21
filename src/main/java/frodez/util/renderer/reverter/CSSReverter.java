package frodez.util.renderer.reverter;

import frodez.util.io.FileUtil;
import frodez.util.renderer.RenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
		Document document = Jsoup.parse(html);
		Elements elements = document.select("link[href]");
		Elements htmlElement = document.select("html");
		elements.forEach((iter) -> {
			try {
				String path = iter.attr("href");
				if (!path.endsWith(".css")) {
					return;
				}
				htmlElement.prepend("<style type=\"text/css\">" + FileUtil.readString(ResourceUtils.getFile(RenderUtil
					.getLoaderPath().concat(path))) + "</style>");
			} catch (Exception e) {
				log.error("[revert]", e);
				return;
			}
		});
		elements.remove();
		return document.html();
	}

}
