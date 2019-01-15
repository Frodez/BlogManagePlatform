package info.frodez;

import info.frodez.util.validation.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

public class ValidationTest {

	public static void main(String[] args) {
		ValidTest item = new ValidTest();
		List<SubValidTest> tests = new ArrayList<>();
		SubValidTest sub1 = new SubValidTest();
		sub1.setSubTest("11");
		SubValidTest sub2 = new SubValidTest();
		sub2.setSubTest("22");
		SubValidTest sub3 = new SubValidTest();
		sub3.setSubTest(" ");
		SubValidTest sub4 = new SubValidTest();
		tests.add(sub1);
		tests.add(sub2);
		tests.add(sub3);
		tests.add(sub4);
		item.setTests(tests);
		System.out.println(ValidationUtil.validate(item));
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
