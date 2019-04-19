package frodez;

import frodez.util.renderer.FreemarkerRender;
import frodez.util.spring.ContextUtil;
import org.springframework.boot.SpringApplication;

public class RenderTest {

	public static void main(String[] args) {
		SpringApplication.run(BlogManagePlatformApplication.class, args);
		int totalTimes = 100000;
		int testTimes = 3;
		for (int i = 1; i <= testTimes; i++) {
			System.out.println("第" + i + "次测试开始!");
			long start = System.currentTimeMillis();
			for (int j = 0; j < totalTimes; j++) {
				FreemarkerRender.render("test");
			}
			start = System.currentTimeMillis() - start;
			System.out.println("第" + i + "次测试结束,测试次数" + totalTimes + "次,耗时" + start + "毫秒");
		}
		SpringApplication.exit(ContextUtil.context(), () -> 1);
	}

}
