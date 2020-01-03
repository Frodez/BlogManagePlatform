package frodez.mybatis;

import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.table.user.User;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void test() {
		userMapper.partialNoCondition("name").forEach(System.out::println);
		userMapper.selectEqual("name", "123");
		userMapper.countByIds(List.of(1L, 2L));
		userMapper.selectIn("id", List.of(1L, 2L));
		List<String> ids = userMapper.partialIn("name", "id", List.of(1L, 2L));
		ids = userMapper.partialByIds("name", List.of(1L, 2L));
		ids.forEach(System.out::println);
		Example example = new Example(User.class);
		example.createCriteria().andIn("id", List.of(1L, 2L));
		userMapper.partialByExample("name", example).forEach(System.out::println);
	}

}
