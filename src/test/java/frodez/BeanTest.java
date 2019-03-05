package frodez;

import frodez.constant.setting.DefRegex;
import frodez.util.beans.result.Result;
import frodez.util.json.JSONUtil;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;
import lombok.Data;

public class BeanTest {

	public static void main(String[] args) {
		Date date = new Date();
		BeanOne one = new BeanOne();
		one.setDate(date);
		Result result = Result.success(one);
		System.out.println(result);
		one.setId(666L);
		System.out.println(result);
		Result result2 = JSONUtil.as(
			"{\"message\":\"成功\",\"code\":1000,\"data\":{\"id\":667,\"number\":null,\"name\":null,\"message\":null,\"isOk\":null,\"type\":null,\"date\":1551535504226}}",
			Result.class);
		System.out.println(result2);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			Result.success().toString();
		}
		System.out.println(System.currentTimeMillis() - start);
		Random random = new Random();
		int times = 10000 * 10000;
		byte[] bytes = new byte[times];
		random.nextBytes(bytes);
		String[] array = new String[times];
		for (int i = 0; i < times; i++) {
			array[i] = Long.toString(13500000000L + bytes[i] * 65536 + i);
		}
		System.out.println(array[0]);
		System.out.println(array[times - 1]);
		start = System.currentTimeMillis();
		Pattern pattern = Pattern.compile(DefRegex.MOBILE);
		for (int i = 0; i < times; i++) {
			pattern.matcher(array[i]).matches();
		}
		System.out.println(System.currentTimeMillis() - start);
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
