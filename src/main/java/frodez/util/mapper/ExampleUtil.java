package frodez.util.mapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import tk.mybatis.mapper.entity.Example;

/**
 * 通用mapperExample工具类
 * @author Frodez
 * @date 2019-03-17
 */
@UtilityClass
public class ExampleUtil {

	private static Map<Class<?>, Example> exampleCache = new ConcurrentHashMap<>();

	/**
	 * 获取对应的example，并且执行clear操作
	 * @author Frodez
	 * @date 2019-03-17
	 */
	public static Example get(Class<?> klass) {
		Example example = exampleCache.computeIfAbsent(klass, i -> new Example(i));
		example.clear();
		return example;
	}

}
