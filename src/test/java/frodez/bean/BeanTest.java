package frodez.bean;

import frodez.dao.model.user.Role;
import frodez.util.reflect.BeanUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanTest {

	@Test
	public void test() throws InvocationTargetException, IllegalArgumentException, IllegalAccessException {
		BeanOne one = new BeanOne();
		BeanOne oneCopy = new BeanOne();
		BeanUtil.cover(one, oneCopy);
		Assert.assertTrue(oneCopy.equals(BeanUtil.initialize(one, BeanOne.class)));
		int thread = 16;
		int total = 300 * 1000;
		int times = 4;
		System.out.println("test");
		for (int i = 1; i <= times; ++i) {
			long start = System.nanoTime();
			execuateCover(one, total, thread);
			long duration = System.nanoTime() - start;
			System.out.println("BeanUtil.cover:" + duration / 1000);
			start = System.nanoTime();
			execuateInitialize(one, total, thread);
			duration = System.nanoTime() - start;
			System.out.println("BeanUtil.initialize:" + duration / 1000);
			System.out.println("--------");
		}
	}

	private void execuateCover(BeanOne one, int total, int thread) {
		for (int i = 0; i < thread; ++i) {
			new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < total; ++i) {
						BeanUtil.cover(one, new BeanOne());
					}
				}
			}.run();
		}
	}

	private void execuateInitialize(BeanOne one, int total, int thread) {
		for (int i = 0; i < thread; ++i) {
			new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < total; ++i) {
						BeanUtil.initialize(one, BeanOne.class);
					}
				}
			}.run();
		}
	}

	@Test
	public void testBatch() {
		Role example = new Role();
		example.setCreateTime(new Date());
		example.setDescription("test123");
		example.setId(123L);
		example.setLevel((byte) 1);
		example.setName("wqwq");
		List<Role> roles = new ArrayList<>();
		int thread = 16;
		int total = 300 * 1000;
		for (int i = 0; i < total; i++) {
			Role role = new Role();
			role.setCreateTime(new Date());
			role.setDescription("test123");
			role.setId(123L);
			role.setLevel((byte) 1);
			role.setName("wqwq");
			roles.add(role);
		}
		int times = 4;
		System.out.println("testBatch");
		for (int i = 1; i <= times; ++i) {
			long start = System.nanoTime();
			execuateCopies(roles, thread);
			start = System.nanoTime() - start;
			System.out.println("BeanUtil.copies:" + start / 1000);
			start = System.nanoTime();
			execuateBatchCopy(example, total, thread);
			start = System.nanoTime() - start;
			System.out.println("BeanUtil.copy:" + start / 1000);
			System.out.println("--------");
		}
	}

	private void execuateCopies(List<Role> roles, int thread) {
		for (int i = 0; i < thread; ++i) {
			new Runnable() {

				@Override
				public void run() {
					BeanUtil.copies(roles, Role.class);
				}
			}.run();
		}
	}

	private void execuateBatchCopy(Role example, int total, int thread) {
		for (int i = 0; i < thread; ++i) {
			new Runnable() {

				@Override
				public void run() {
					List<Role> copies = new ArrayList<>(total);
					for (int i = 0; i < total; ++i) {
						Role copy = new Role();
						BeanUtil.copy(example, copy);
						copies.add(copy);
					}
				}
			}.run();
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

		private Long id = 1L;

		private String number = "123";

		private String name = "www";

		private String message;

		private Boolean isOk = true;

		private Byte type = (byte) 9;

		private Date date = new Date();

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
