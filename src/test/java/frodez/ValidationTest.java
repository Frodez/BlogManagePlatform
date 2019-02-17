package frodez;

import frodez.util.common.DateUtil;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

public class ValidationTest {

	public static void main(String[] args) {
		//		Random random = new Random();
		//		byte[] array = new byte[100];
		//		random.nextBytes(array);
		//		for (byte iter : array) {
		//			System.out.println(iter);
		//		}
		System.out.println(DateUtil.dateTime("2019-02-17 18:22:34"));
		//		Result result = ResultUtil.page(10, new ArrayList<String>());
		//		String json = JSONUtil.toJSONString(result);
		//		System.out.println(json);
		//		Map<String, Object> map = JSONUtil.toMap(json);
		//		System.out.println(map.toString());
		//		System.out.println(JSONUtil.toJSONString(map));
		//		ValidTest item = new ValidTest();
		//		List<SubValidTest> tests = new ArrayList<>();
		//		SubValidTest sub1 = new SubValidTest();
		//		sub1.setSubTest("11");
		//		SubValidTest sub2 = new SubValidTest();
		//		sub2.setSubTest("22");
		//		SubValidTest sub3 = new SubValidTest();
		//		sub3.setSubTest(" ");
		//		SubValidTest sub4 = new SubValidTest();
		//		tests.add(sub1);
		//		tests.add(sub2);
		//		tests.add(sub3);
		//		tests.add(sub4);
		//		item.setTests(tests);
		//		System.out.println(ValidationUtil.validate(item));
	}

	@Data
	public static class ValidTest {

		@Valid
		@NotEmpty(message = "列表不能为空!")
		public List<SubValidTest> tests;

	}

	@Data
	public static class SubValidTest {

		@NotNull(message = "subTest can't be null!")
		@NotBlank(message = "subTest can't be blank!")
		private String subTest;

	}

}
