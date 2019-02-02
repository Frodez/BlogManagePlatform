package info.frodez;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import frodez.BlogManagePlatformApplication;
import frodez.config.aop.request.checker.facade.RepeatChecker;
import frodez.config.aop.request.checker.impl.RepeatRedisChecker;
import frodez.util.spring.context.ContextUtil;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class RedisTest {

	@Test
	public void test() {
		//		Map<Object, Object> map = new HashMap<>();
		//		map.put("1", 101);
		//		map.put("2", 102);
		//		map.put("3", 103);
		//		redisService.hmset("testhm", map);
		//		System.out.println(redisService.hmexists("testhm"));
		//		redisService.delete("testhm");
		//		//redisService.deletePattern("testhm");
		//		System.out.println(redisService.hmexists("testhm"));
	}

	public static void main(String[] args) {
		SpringApplication.run(BlogManagePlatformApplication.class, args);
		RepeatChecker checker = ContextUtil.getBean(RepeatRedisChecker.class);
		//RepeatChecker checker = ContextUtil.getBean(RepeatGuavaChecker.class);
		Long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			String key = i + "";
			checker.check(key);
			checker.lock(key);
			checker.free(key);
		}
		System.out.println("time:" + (System.currentTimeMillis() - start));
	}

}
