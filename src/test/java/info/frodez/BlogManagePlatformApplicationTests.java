package info.frodez;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;

import info.frodez.util.spring.properties.SpringProperties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagePlatformApplicationTests {

	@Autowired
	private DataSourceTransactionManager manager;

	@Autowired
	private SpringProperties properties;

	@Test
	public void contextLoads() {
		Environment env = properties.getEnv();
		Assert.assertNotNull(properties);
		Assert.assertNotNull(env);
		Assert.assertNotNull(manager);
	}

}
