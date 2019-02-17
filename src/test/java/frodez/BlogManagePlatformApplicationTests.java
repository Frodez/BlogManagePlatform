package frodez;

import frodez.util.spring.properties.PropertyUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagePlatformApplicationTests {

	@Autowired
	private DataSourceTransactionManager manager;

	@Autowired
	private PropertyUtil properties;

	@Test
	public void contextLoads() {
		Environment env = properties.getEnv();
		Assert.assertNotNull(properties);
		Assert.assertNotNull(env);
		Assert.assertNotNull(manager);
	}

}
