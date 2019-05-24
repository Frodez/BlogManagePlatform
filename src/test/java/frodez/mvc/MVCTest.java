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
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
public class MVCTest {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void escapeTest() {
		Result result = webClient.get().uri(PropertyUtil.get(PropertyKey.Web.BASE_PATH)
			+ "/test/escape?name=<script>var data = $SomeJacksonWrapper.toJson($data);</script>").exchange().expectBody(
				Result.class).returnResult().getResponseBody();
		Assert.assertFalse(result.unable());
	}

}
