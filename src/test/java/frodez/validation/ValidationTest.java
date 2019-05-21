package frodez.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import frodez.config.aop.validation.annotation.special.DateTime;
import frodez.constant.enums.common.ModifyEnum;
import frodez.dao.param.user.AddPermission;
import frodez.dao.param.user.QueryRolePermission;
import frodez.dao.param.user.UpdateRolePermission;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.common.ValidationUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
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

	@Test
	public void test1() {
		UpdateRolePermission bean = new UpdateRolePermission();
		bean.setRoleId(1L);
		bean.setOperationType(ModifyEnum.UPDATE.getVal());
		bean.setPermissionIds(Arrays.asList(1L, -2L, 3L));
		System.out.println(ValidationUtil.validate(bean));
	}

	@Test
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
		addPermission.setUrl("33333");
		addPermission.setDescription("3333333");
		addPermission.setType((byte) -1);
		result = authorityService.addPermission(addPermission);
		System.out.println(result.json());
		ValidationBean bean = new ValidationBean();
		bean.setDate("1999-01-12 11:22:33");
		System.out.println(ValidationUtil.validate(bean));
	}

	@Data
	public static class ValidationBean {

		@DateTime
		private String date;

	}

}
