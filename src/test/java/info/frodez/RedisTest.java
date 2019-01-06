package info.frodez;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import info.frodez.service.redis.RedisService;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class RedisTest {

	@Autowired
	private RedisService redisService;

	@Test
	public void test() {
		Map<Object, Object> map = new HashMap<>();
		map.put("1", 101);
		map.put("2", 102);
		map.put("3", 103);
		redisService.hmset("testhm", map);
		System.out.println(redisService.hmexists("testhm"));
		redisService.delete("testhm");
		//redisService.deletePattern("testhm");
		System.out.println(redisService.hmexists("testhm"));
	}

}
