package frodez;

import frodez.dao.model.user.Role;
import frodez.util.beans.result.Result;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import org.springframework.boot.SpringApplication;

public class ResultTest {

	@SuppressWarnings({ "resource" })
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		SpringApplication.run(BlogManagePlatformApplication.class, args);
		Role role = new Role();
		role.setCreateTime(new Date());
		role.setDescription("test123");
		role.setId(123L);
		role.setLevel((byte) 1);
		role.setName("wqwq");
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("D:/test.txt"));
		outputStream.writeObject(Result.success(role));
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("D:/test.txt"));
		Result object = (Result) inputStream.readObject();
		object.getClass();
	}

}
