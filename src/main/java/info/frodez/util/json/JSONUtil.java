package info.frodez.util.json;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * json工具类
 * @author Frodez
 * @date 2018-11-27
 */
public class JSONUtil {

	private static ObjectMapper objectMapper = new ObjectMapper()
		.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
		.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true)
		.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

	private static TypeFactory typeFactory = objectMapper.getTypeFactory();

	/**
	 * 获取jackson对象
	 * @author Frodez
	 * @date 2018-12-02
	 */
	public static ObjectMapper getInstance() {
		return objectMapper;
	}

	/**
	 * 将对象转换成json字符串,发生异常返回null
	 * @author Frodez
	 * @param object 对象
	 * @date 2018-12-02
	 */
	public static String toJSONString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	/**
	 * 将json字符串转换成对象,发生异常返回null
	 * @author Frodez
	 * @param json json字符串
	 * @param klass 对象类型
	 * @date 2018-12-02
	 */
	public static <T> T toObject(String json, Class<T> klass) {
		try {
			return objectMapper.readValue(json, klass);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将json字符串转换成Map,发生异常返回null
	 * @author Frodez
	 * @param <K>
	 * @param <V>
	 * @param json json字符串
	 * @date 2018-12-02
	 */
	public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
		try {
			JavaType type = typeFactory.constructParametricType(Map.class, keyClass, valueClass);
			return objectMapper.readValue(json, type);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将json字符串转换成List,发生异常返回null
	 * @author Frodez
	 * @param <T>
	 * @param json json字符串
	 * @date 2018-12-02
	 */
	public static <T> List<T> toList(String json, Class<T> klass) {
		try {
			JavaType type = typeFactory.constructParametricType(List.class, klass);
			return objectMapper.readValue(json, type);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		// Map<String, Result> map = new HashMap<>();
		// map.put("111", new Result(ResultEnum.SUCCESS, "111"));
		// map.put("222", new Result(ResultEnum.SUCCESS, "222"));
		// map.put("333", new Result(ResultEnum.SUCCESS, "333"));
		// String json = JSONUtil.toJSONString(map);
		// Map<String, Result> resultMap = JSONUtil.toMap(json, String.class,
		// Result.class);
		// String json = JSONUtil.toJSONString(Arrays.asList(new
		// Result(ResultEnum.SUCCESS, "222"),
		// new Result(ResultEnum.SUCCESS, "222"), new Result(ResultEnum.SUCCESS,
		// "222")));
		// System.out.println(json);
		// List<Result> results = JSONUtil.toList(json, Result.class);
		// Result result = results.get(0);
		// System.out.println(result.toString());
	}

}
