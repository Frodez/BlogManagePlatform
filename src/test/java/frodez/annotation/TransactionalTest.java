package frodez.annotation;

import frodez.util.beans.result.Result.ResultEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionalTest {

	@Autowired
	private TransactionalServiceA transactionalService;

	@Test
	public void testCatchAndReturn() {
		Assert.assertTrue(transactionalService.run().getCode() == ResultEnum.ERROR_SERVICE.getVal());
	}

}
