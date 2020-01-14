package mybatis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class Generator {

	@SneakyThrows
	public static void main(String[] args) {
		List<String> warnings = new ArrayList<>();
		ConfigurationParser configurationParser = new ConfigurationParser(warnings);
		Configuration configuration = configurationParser.parseConfiguration(new File(Generator.class.getResource("").getPath() + "generator.xml"));
		DefaultShellCallback callback = new DefaultShellCallback(true);
		MyBatisGenerator generator = new MyBatisGenerator(configuration, callback, warnings);
		generator.generate(new VerboseProgressCallback());
		for (String warn : warnings) {
			System.out.println(warn);
		}
	}

}
