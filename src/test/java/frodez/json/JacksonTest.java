package frodez.json;

import frodez.util.beans.param.QueryPage;
import frodez.util.json.JSONUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JacksonTest {

	@Test
	public void test() throws IOException {
		Map<String, Object> map = new HashMap<>();
		Date date = new Date();
		BigDecimal bigDecimal = new BigDecimal("12.555251635");
		String string = "testString";
		map.put("date", date);
		map.put("decimal", new BigDecimal("12.555251635"));
		map.put("string", string);
		map.put("object", QueryPage.DEFAULT);
		JacksonTestBean bean = JSONUtil.as(JSONUtil.string(map), JacksonTestBean.class);
		Assert.assertNotNull(bean.getDate().equals(date));
		Assert.assertNotNull(bean.getDecimal().equals(bigDecimal));
		Assert.assertNotNull(bean.getString().equals(string));
		Assert.assertNotNull(bean.getObject().equals(QueryPage.DEFAULT));
	}

	@Data
	public static class JacksonTestBean {

		private Date date;

		private BigDecimal decimal;

		private String string;

		private QueryPage object;

	}

}
