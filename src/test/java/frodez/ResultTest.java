package frodez;

import frodez.dao.model.user.Role;
import frodez.util.json.JSONUtil;
import frodez.util.reflect.BeanUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.boot.SpringApplication;

public class ResultTest {

	@SuppressWarnings({ "unused" })
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		SpringApplication.run(BlogManagePlatformApplication.class, args);
		Role role = new Role();
		role.setCreateTime(new Date());
		role.setDescription("test123");
		role.setId(123L);
		role.setLevel((byte) 1);
		role.setName("wqwq");
		String json = JSONUtil.string(role);
		Map<String, Object> map = JSONUtil.map(json);
		for (Entry<String, Object> entry : map.entrySet()) {
			Object key = entry.getKey();
			Class<?> keyClass = key.getClass();
			Object value = entry.getValue();
			Class<?> valueClass = value.getClass();
		}
		role = BeanUtil.as(map, Role.class);
		//		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("D:/test.txt"));
		//		outputStream.writeObject(Result.success(role));
		//		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("D:/test.txt"));
		//		Result object = (Result) inputStream.readObject();
		//		object.getClass();
	}

}
