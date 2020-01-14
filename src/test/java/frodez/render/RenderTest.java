package frodez.render;

import frodez.util.io.FileUtil;
import frodez.util.pdf.PDFConverter;
import frodez.util.renderer.FreemarkerRender;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RenderTest {

	@Autowired
	PDFConverter converter;

	@Test
	public void test() throws InterruptedException, ExecutionException, IOException, URISyntaxException {
		String path = FileUtil.PATH + "test.pdf";
		System.out.println(path);
		ByteArrayOutputStream stream = converter.convert(FreemarkerRender.render("test")).get();
		FileUtil.writeBytes(stream.toByteArray(), ResourceUtils.getFile(path));
	}

}
