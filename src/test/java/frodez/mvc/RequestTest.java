package frodez.mvc;

import frodez.constant.settings.PropertyKey;
import frodez.util.beans.result.Result;
import frodez.util.spring.PropertyUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "spring.profiles.active=test" }, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
public class RequestTest {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void test() {
		Result result = webClient.get().uri(PropertyUtil.get(PropertyKey.Web.BASE_PATH) + "/login/test?userName=123")
			.exchange().expectBody(Result.class).returnResult().getResponseBody();
		Assert.assertFalse(result.unable());
	}

}
