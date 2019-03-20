package frodez;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import frodez.util.spring.ContextUtil;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class RedisTest {

	public static void main(String[] args) throws TemplateNotFoundException, MalformedTemplateNameException,
		ParseException, IOException, TemplateException {
		SpringApplication.run(BlogManagePlatformApplication.class, args);
		Configuration configuration = ContextUtil.get(Configuration.class);
		Template template = configuration.getTemplate("test.ftl");
		StringWriter writer = new StringWriter();
		template.process(new HashMap<>(), writer);
		System.out.println(writer.toString());
		//RepeatChecker checker = ContextUtil.getBean(RepeatRedisChecker.class);
		//		ManualChecker checker = ContextUtil.getBean(ManualGuavaChecker.class);
		//		Long start = System.currentTimeMillis();
		//		for (int i = 0; i < 10000000; i++) {
		//			String key = i + "";
		//			checker.check(key);
		//			checker.lock(key);
		//			checker.free(key);
		//		}
		//		System.out.println("time:" + (System.currentTimeMillis() - start));
		//		SpringApplication.exit(ContextUtil.get(), () -> 1);
	}

}
