package frodez.codecheck;

import frodez.config.aop.util.AOPUtil;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.beans.result.Result;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeCheckTest {

	@Test
	public void test1() throws NoSuchMethodException, SecurityException {
		Method resultMethod = CodeCheckTest.class.getMethod("resultMethod", new Class<?>[] {});
		Method asyncResultMethod = CodeCheckTest.class.getMethod("asyncResultMethod", new Class<?>[] {});
		Assert.assertTrue(AOPUtil.isResultAsReturn(resultMethod));
		Assert.assertTrue(AOPUtil.isAsyncResultAsReturn(asyncResultMethod));
	}

	@Test(expected = CodeCheckException.class)
	public void test2() throws NoSuchMethodException, SecurityException {
		Method wrongMethod = CodeCheckTest.class.getMethod("wrongMethod", new Class<?>[] {});
		AOPUtil.isResultAsReturn(wrongMethod);
	}

	public Result resultMethod() {
		return Result.success();
	}

	public Boolean wrongMethod() {
		return true;
	}

	public ListenableFuture<Result> asyncResultMethod() {
		return Result.success().async();
	}

}
