package frodez.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncTest {

	@Autowired
	private AsyncService asyncService;

	@Test
	public void testAsync() throws InterruptedException, ExecutionException, JsonProcessingException {
		ListenableFuture<Result> future1 = asyncService.async(new QueryPage(-1, 20));
		ListenableFuture<Result> future2 = asyncService.async(new QueryPage(-1, 20));
		Result result1 = future1.get();
		Result result2 = future2.get();
		String string1 = result1.json();
		String string2 = result2.json();
		System.out.println(string1);
		System.out.println(string2);
	}

	@Test
	public void test() throws JsonProcessingException {
		Result result1 = asyncService.result(new QueryPage(-1, 20));
		Result result2 = asyncService.result(new QueryPage(-1, 20));
		String string1 = result1.json();
		String string2 = result2.json();
		System.out.println(string1);
		System.out.println(string2);
	}

}
