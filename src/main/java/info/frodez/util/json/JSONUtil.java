package info.frodez.util.json;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import info.frodez.util.result.Result;
import info.frodez.util.result.ResultEnum;

/**
 * json工具类
 * @author Frodez
 * @date 2018-11-27
 */
public class JSONUtil {

	private static ObjectMapper objectMapper = new ObjectMapper()
		.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
		.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);

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
			return getInstance().writeValueAsString(object);
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
			return getInstance().readValue(json, klass);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将json字符串转换成Map,发生异常返回null
	 * @author Frodez
	 * @param json json字符串
	 * @date 2018-12-02
	 */
	@SuppressWarnings("rawtypes")
	public static Map toMap(String json) {
		try {
			return getInstance().readValue(json, Map.class);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将json字符串转换成List,发生异常返回null
	 * @author Frodez
	 * @param json json字符串
	 * @date 2018-12-02
	 */
	@SuppressWarnings("rawtypes")
	public static List toList(String json) {
		try {
			return getInstance().readValue(json, List.class);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(JSONUtil.toJSONString(new Result(ResultEnum.SUCCESS, "222")));
	}

}
