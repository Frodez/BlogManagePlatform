package frodez;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import frodez.util.beans.result.Result;
import frodez.util.json.JSONUtil;
import java.util.Date;
import lombok.Data;
import org.springframework.boot.SpringApplication;

public class BeanTest {

	public static void main(String[] args) {
		SpringApplication.run(BlogManagePlatformApplication.class, args);
		Date date = new Date();
		BeanOne one = new BeanOne();
		one.setDate(date);
		Result result = Result.success(one);
		int quantity = 1000 * 10000;
		for (int time = 0; time < 5; time++) {
			long start = System.currentTimeMillis();
			long duration = 0;
			for (int i = 0; i < quantity; i++) {
				JSONUtil.string(result);
			}
			duration = System.currentTimeMillis() - start;
			System.out.println("v1");
			System.out.println(duration);
			System.out.println("------------------------------");
			ObjectWriter writer = JSONUtil.mapper().writerWithView(Result.class);
			start = System.currentTimeMillis();
			for (int i = 0; i < quantity; i++) {
				try {
					writer.writeValueAsString(result);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//JSONUtil.listV2(string, Integer.class);
			}
			duration = System.currentTimeMillis() - start;
			System.out.println("v2");
			System.out.println(duration);
			System.out.println("------------------------------");
		}
		//		BeanUtil.getProperties(Role.class);
		//		System.out.println(BeanUtil.isClear(new Role()));
		//		System.out.println(BeanUtil.isClear(BeanUtil.clearInstance(Role.class)));
		//		long start = System.currentTimeMillis();
		//		for (int i = 0; i < 10000 * 10000; i++) {
		//			//BeanUtil.isClear(new Role());
		//			BeanUtil.clear(new Role());
		//		}
		//		System.out.println(System.currentTimeMillis() - start);
		//		PageQuery page = new PageQuery(1, 2);
		//		String json = JSONUtil.string(page);
		//		System.out.println(json);
		//		page = JSONUtil.as(json, PageQuery.class);
		//		json = JSONUtil.string(page);
		//		System.out.println(json);
		//		Date date = new Date();
		//		BeanOne one = new BeanOne();
		//		one.setDate(date);
		//		Result result = Result.success(one);
		//		System.out.println(result);
		//		one.setId(666L);
		//		System.out.println(result);
		//		Result result2 = JSONUtil.as(
		//			"{\"message\":\"成功\",\"code\":1000,\"data\":{\"id\":667,\"number\":null,\"name\":null,\"message\":null,\"isOk\":null,\"type\":null,\"date\":1551535504226}}",
		//			Result.class);
		//		System.out.println(result2);
		//		long start = System.currentTimeMillis();
		//		for (int i = 0; i < 100000000; i++) {
		//			Result.success().toString();
		//		}
		//		System.out.println(System.currentTimeMillis() - start);
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
