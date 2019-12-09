package frodez.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.config.aop.validation.annotation.special.DateTime;
import frodez.config.code.checker.CodeChecker;
import frodez.config.validator.ValidationUtil;
import frodez.constant.enums.common.OperateEnum;
import frodez.dao.param.user.AddPermission;
import frodez.dao.param.user.QueryRolePermission;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidationTest {

	@Autowired
	private IAuthorityService authorityService;

	@Autowired
	CodeChecker codeChecker;

	//@Test
	public void test() {
		codeChecker.checkClass(ValidationBean.class);
		codeChecker.checkClass(InnerBean.class);
	}

	@Test
	public void test1() {
		ValidationBean bean = new ValidationBean();
		InnerBean innerBean = new InnerBean();
		innerBean.setNumber(1);
		innerBean.setString("1");
		innerBean.setType((byte) 1);
		InnerBean innerBean1 = new InnerBean();
		innerBean1.setNumber(-1);
		innerBean1.setList(Arrays.asList(2, 4, 5));
		innerBean1.setString("");
		innerBean1.setType((byte) 1);
		InnerBean innerBean2 = new InnerBean();
		innerBean2.setNumber(2);
		innerBean2.setList(Arrays.asList(2, 4, 5));
		innerBean2.setString("2");
		innerBean2.setType((byte) 9);
		bean.setDate("1999-01-12 11:22:33:8");
		bean.setBean(innerBean1);
		bean.setBeans(Arrays.asList(Arrays.asList(innerBean1, innerBean), Arrays.asList(innerBean, innerBean2)));
		//System.out.println(ValidationUtil.validate(innerBean));
		System.out.println(ValidationUtil.validate(bean));
	}

	//@Test
	public void test2() throws JsonProcessingException, InvocationTargetException {
		QueryRolePermission param = new QueryRolePermission();
		param.setRoleId(1L);
		QueryPage page = new QueryPage(1, 3000);
		param.setPage(page);
		Result result = authorityService.getRolePermissions(param);
		System.out.println(result.json());
		result = authorityService.getUserInfo("");
		System.out.println(result.json());
		AddPermission addPermission = new AddPermission();
		addPermission.setName("2222");
		addPermission.setUrl(null);
		addPermission.setDescription(null);
		addPermission.setType((byte) -1);
		result = authorityService.addPermission(addPermission);
		System.out.println(result.json());
	}

	@Data
	public static class ValidationBean {

		@DateTime
		private String date;

		@Valid
		private InnerBean bean;

		@Valid
		@NotEmpty
		private List<@Valid List<@Valid InnerBean>> beans;

	}

	@Data
	public static class InnerBean {

		@Positive
		private Integer number;

		@NotBlank
		private String string;

		@NotEmpty
		private List<Integer> list;

		@MapEnum(value = OperateEnum.class, paramType = Byte.class)
		private Byte type;

	}

}
