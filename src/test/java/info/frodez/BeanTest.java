package info.frodez;

import java.util.Date;
import lombok.Data;

public class BeanTest {

	public static void main(String[] args) {

		Date date = new Date();
		BeanOne one = new BeanOne();
		one.setId((long) 1);
		one.setNumber("1");
		one.setName("frodez");
		one.setMessage("hello");
		one.setIsOk(true);
		one.setType((byte) 5);
		one.setDate(date);
		one.setDate(date);
		//		long start = System.currentTimeMillis();
		//		for (int i = 0; i < 100000000; i++) {
		//			Map<String, Object> map = BeanUtil.asMap(one);
		//		}
		//		System.out.println(System.currentTimeMillis() - start);
		//		Map<String, Object> map = BeanUtil.asMap(one);
		//		map.put("name", "Frodez");
		//		BeanOne one2 = new BeanOne();
		//		BeanUtil.asBean(map, one2);
		//		long start = System.currentTimeMillis();
		//		for(int i = 0; i < 100000000; i++) {
		//			if((i & 0b11) == 0) {
		//				BeanTwo two = new BeanTwo();
		//				//BeanUtils.copyProperties(one, two);
		//				BeanUtil.copy(one, two);
		//			}
		//			if((i & 0b11) == 1) {
		//				BeanThree three = new BeanThree();
		//				//BeanUtils.copyProperties(one, three);
		//				BeanUtil.copy(one, three);
		//			}
		//			if((i & 0b11) == 2) {
		//				BeanFour four = new BeanFour();
		//				//BeanUtils.copyProperties(one, four);
		//				BeanUtil.copy(one, four);
		//			}
		//			if((i & 0b11) == 3) {
		//				BeanFive five = new BeanFive();
		//				//BeanUtils.copyProperties(one, five);
		//				BeanUtil.copy(one, five);
		//			}
		//		}
		//		System.out.println(System.currentTimeMillis() - start);
		//		start = System.currentTimeMillis();
		//		for(int i = 0; i < 100000000; i++) {
		//			if((i & 0b11) == 0) {
		//				BeanTwo two = new BeanTwo();
		//				BeanUtils.copyProperties(one, two);
		//				//BeanUtil.copy(one, two);
		//			}
		//			if((i & 0b11) == 1) {
		//				BeanThree three = new BeanThree();
		//				BeanUtils.copyProperties(one, three);
		//				//BeanUtil.copy(one, three);
		//			}
		//			if((i & 0b11) == 2) {
		//				BeanFour four = new BeanFour();
		//				BeanUtils.copyProperties(one, four);
		//				//BeanUtil.copy(one, four);
		//			}
		//			if((i & 0b11) == 3) {
		//				BeanFive five = new BeanFive();
		//				BeanUtils.copyProperties(one, five);
		//				//BeanUtil.copy(one, five);
		//			}
		//		}
		//		System.out.println(System.currentTimeMillis() - start);
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
