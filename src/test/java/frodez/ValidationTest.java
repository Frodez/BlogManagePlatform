package frodez;

import com.fasterxml.jackson.core.JsonProcessingException;
import frodez.dao.param.user.QueryRolePermission;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
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
	public void test() throws JsonProcessingException {
		QueryRolePermission param = new QueryRolePermission();
		param.setRoleId(1L);
		QueryPage page = new QueryPage(-1, 8);
		param.setPage(page);
		Result result = authorityService.getRolePermissions(param);
		System.out.println(result.json());
	}

}
