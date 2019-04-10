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
import frodez.util.io.FileUtil;
import frodez.util.spring.ContextUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 * PDF生成工具类<br>
 * <strong>警告!!!如果要使用本类的方法,必须确保PDFConverter已经被初始化!</strong><br>
 * <strong>方式:在使用本方法的类上加入@DependsOn("PDFConverter")注解。</strong>
 * @author Frodez
 * @date 2019-03-27
 */
@Lazy
@Slf4j
@Component("PDFConverter")
public class PDFConverter {

	private static Map<String, FontProgram> fontCache = new HashMap<>();

	@PostConstruct
	private void init() {
		FontProperties properties = ContextUtil.get(FontProperties.class);
		try {
			for (Entry<String, String> entry : properties.getAlias().entrySet()) {
				fontCache.put(entry.getKey(), FontProgramFactory.createFont(FileUtil.readBytes(ResourceUtils.getFile(
					properties.getPath().concat(entry.getValue()))), false));
			}
		} catch (IOException e) {
			log.error("[init]", e);
		}
	}

	/**
	 * 获取所有的字体<br>
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static Map<String, FontProgram> fonts() {
		return fontCache;
	}

	/**
	 * 将html内容转换为pdf
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public static ByteArrayOutputStream convert(String html) throws IOException {
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
		return stream;
	}

}
