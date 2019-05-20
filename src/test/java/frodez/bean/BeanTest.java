package frodez.bean;

import frodez.dao.model.user.Role;
import frodez.util.reflect.BeanUtil;
import java.util.Date;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanTest {

	@Test
	public void test() {
		Role role = new Role();
		role.setCreateTime(new Date());
		role.setDescription("test123");
		role.setId(123L);
		role.setLevel((byte) 1);
		role.setName("wqwq");
		int total = 100 * 10000;
		int times = 5;
		for (int i = 1; i <= times; ++i) {
			long start = System.currentTimeMillis();
			for (int j = 0; j < total; ++j) {
				BeanUtil.cover(role, new Role());
			}
			long duration = System.currentTimeMillis() - start;
			System.out.println(duration);
			start = System.currentTimeMillis();
			for (int j = 0; j < total; ++j) {
				BeanUtil.initialize(role, Role.class);
			}
			duration = System.currentTimeMillis() - start;
			System.out.println(duration);
		}
	}

	@Data
	public static class BeanOne {

		private Long id;

		private String number;

		private String name;

		private String message;

		private Boolean isOk;

		private Byte type;

		private Date date;

	}

	@Data
	public static class BeanTwo {

		private Long id;

		private String number;

		private String name;

		private String message;

		private Boolean isOk;

		private Byte type;

		private Date date;

	}

	@Data
	public static class BeanThree {

		private Long id;

		private String number;

		private String name;

		private String message;

		private Boolean isOk;

		private Byte type;

		private Date date;

	}

	@Data
	public static class BeanFour {

		private Long id;

		private String number;

		private String name;

		private String message;

		private Boolean isOk;

		private Byte type;

		private Date date;

	}

	@Data
	public static class BeanFive {

		private Long id;

		private String number;

		private String name;

		private String message;

		private Boolean isOk;

		private Byte type;

		private Date date;

	}

}
