package frodez;

import frodez.util.io.FileUtil;
import frodez.util.pdf.PDFConverter;
import frodez.util.renderer.FreemarkerRender;
import frodez.util.spring.ContextUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import org.springframework.boot.SpringApplication;

public class RenderTest {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException,
		URISyntaxException {
		SpringApplication.run(BlogManagePlatformApplication.class, args);
		//		int totalTimes = 100000;
		//		int testTimes = 3;
		//		for (int i = 1; i <= testTimes; i++) {
		//			System.out.println("第" + i + "次测试开始!");
		//			long start = System.currentTimeMillis();
		//			for (int j = 0; j < totalTimes; j++) {
		//				FreemarkerRender.render("test");
		//			}
		//			start = System.currentTimeMillis() - start;
		//			System.out.println("第" + i + "次测试结束,测试次数" + totalTimes + "次,耗时" + start + "毫秒");
		//		}
		PDFConverter converter = ContextUtil.get(PDFConverter.class);
		ByteArrayOutputStream stream = converter.convert(FreemarkerRender.render("test")).get();
		FileUtil.writeBytes(stream.toByteArray(), "file:/D:/test.pdf");
		SpringApplication.exit(ContextUtil.context(), () -> 1);
	}

}
