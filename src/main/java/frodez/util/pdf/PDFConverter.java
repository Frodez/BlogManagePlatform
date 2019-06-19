package frodez.util.pdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import frodez.config.font.FontProperties;
import frodez.util.common.StrUtil;
import frodez.util.io.FileUtil;
import frodez.util.spring.ContextUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * PDF生成工具类<br>
 * @author Frodez
 * @date 2019-03-27
 */
@Lazy
@Slf4j
@Component
public class PDFConverter {

	private static Map<String, FontProgram> fontCache = new HashMap<>();

	@PostConstruct
	private void init() {
		FontProperties properties = ContextUtil.bean(FontProperties.class);
		try {
			for (Entry<String, String> entry : properties.getAlias().entrySet()) {
				fontCache.put(entry.getKey(), FontProgramFactory.createFont(FileUtil.readBytes(ResourceUtils.getFile(
					StrUtil.concat(properties.getPath(), entry.getValue()))), false));
			}
		} catch (IOException e) {
			log.error("[frodez.util.pdf.PDFConverter.init]", e);
		}
	}

	/**
	 * 添加字体
	 * @author Frodez
	 * @date 2019-05-19
	 */
	@SneakyThrows
	public static void addFont(String alias, String fileName) {
		FontProperties properties = ContextUtil.bean(FontProperties.class);
		fontCache.put(alias, FontProgramFactory.createFont(FileUtil.readBytes(ResourceUtils.getFile(StrUtil.concat(
			properties.getPath(), fileName))), false));
	}

	/**
	 * 获取所有的字体<br>
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static Map<String, FontProgram> fonts() {
		return Collections.unmodifiableMap(fontCache);
	}

	/**
	 * 将html内容转换为pdf
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Async
	@SneakyThrows
	public ListenableFuture<ByteArrayOutputStream> convert(String html) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ConverterProperties properties = new ConverterProperties();
		DefaultFontProvider defaultFontProvider = new DefaultFontProvider(false, false, false);
		for (FontProgram font : fontCache.values()) {
			defaultFontProvider.addFont(font);
		}
		properties.setFontProvider(defaultFontProvider);
		PdfDocument pdf = new PdfDocument(new PdfWriter(stream));
		Document document = HtmlConverter.convertToDocument(html, pdf, properties);
		document.close();
		return new AsyncResult<>(stream);
	}

}
