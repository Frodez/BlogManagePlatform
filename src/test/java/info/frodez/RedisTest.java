package info.frodez;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class RedisTest {

	//	@Autowired
	//	private RedisService redisService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void test() {
		System.out.println(passwordEncoder.encode("11111111"));
		//		Map<Object, Object> map = new HashMap<>();
		//		map.put("1", 101);
		//		map.put("2", 102);
		//		map.put("3", 103);
		//		redisService.hmset("testhm", map);
		//		System.out.println(redisService.hmexists("testhm"));
		//		redisService.deletePattern("testhm");
		//		System.out.println(redisService.hmexists("testhm"));
	}

}
