package frodez;

import frodez.dao.model.user.Role;
import frodez.util.reflect.BeanUtil;
import java.util.Date;
import lombok.Data;

public class BeanTest {

	public static void main(String[] args) {
		Role role = new Role();
		role.setCreateTime(new Date());
		role.setDescription("test123");
		role.setId(123L);
		role.setLevel((byte) 1);
		role.setName("wqwq");
		int total = 10000 * 10000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < total; ++i) {
			BeanUtil.cover(role, new Role());
		}
		long duration = System.currentTimeMillis() - start;
		System.out.println(duration);
		start = System.currentTimeMillis();
		for (int i = 0; i < total; ++i) {
			BeanUtil.initialize(role, Role.class);
		}
		duration = System.currentTimeMillis() - start;
		System.out.println(duration);
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
