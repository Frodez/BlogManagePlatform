package frodez.util.json;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import frodez.util.spring.context.ContextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * json工具类
 * @author Frodez
 * @date 2018-11-27
 */
@Component
public class JSONUtil {

	/**
	 * 整个系统中所有的objectMapper均由此处提供,一是减少无用对象,<br>
	 * 二是保证系统中所有使用objectMapper的方法均保持一致的行为,<br>
	 * 而不必担心不同处objectMapper配置不一致导致行为不一致.
	 */
	private static ObjectMapper OBJECT_MAPPER = ContextUtil.get(ObjectMapper.class);

	private static TypeFactory TYPE_FACTORY;

	private static JavaType DEFAULT_MAP_TYPE;

	private static final Map<Class<?>, JavaType> collectionTypeCache = new ConcurrentHashMap<>();

	private static final Map<String, JavaType> mapTypeCache = new ConcurrentHashMap<>();

	/**
	 * 增加危险字符的转义处理.由于统一使用json返回,因此可以视为所有的返回值中的危险字符均已处理.<br>
	 * 另外,由于mybatis中采取预编译的方式注入参数(使用#{}标识符而非${}),sql注入的风险也基本解除.<br>
	 */
	@PostConstruct
	private void init() {
		TYPE_FACTORY = OBJECT_MAPPER.getTypeFactory();
		DEFAULT_MAP_TYPE = TYPE_FACTORY.constructParametricType(HashMap.class, String.class, Object.class);
		OBJECT_MAPPER.getFactory().setCharacterEscapes(new CharacterEscapes() {

			private static final long serialVersionUID = 1L;

			private int[] asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();

			// Note: "&apos;" is not defined in HTML 4.01.
			private Escaper escaper = Escapers.builder().addEscape('"', "&quot;").addEscape('\'', "&#39;").addEscape(
				'&', "&amp;").addEscape('<', "&lt;").addEscape('>', "&gt;").build();

			{
				asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
				asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
				asciiEscapes['&'] = CharacterEscapes.ESCAPE_CUSTOM;
				asciiEscapes['"'] = CharacterEscapes.ESCAPE_CUSTOM;
				asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
			}

			@Override
			public SerializableString getEscapeSequence(int ch) {
				return new SerializedString(escaper.escape(Character.toString(ch)));
			}

			@Override
			public int[] getEscapeCodesForAscii() {
				return asciiEscapes;
			}
		});
	}

	/**
	 * 获取jackson对象
	 * @author Frodez
	 * @date 2018-12-02
	 */
	public static ObjectMapper mapper() {
		return OBJECT_MAPPER;
	}

	/**
	 * 将对象转换成json字符串
	 * @author Frodez
	 * @param object 对象
	 * @date 2018-12-02
	 */
	public static String string(Object object) {
		try {
			return OBJECT_MAPPER.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将json字符串转换成对象
	 * @author Frodez
	 * @param json json字符串
	 * @param klass 对象类型
	 * @date 2018-12-02
	 */
	public static <T> T as(String json, Class<T> klass) {
		try {
			return OBJECT_MAPPER.readValue(json, klass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将json字符串转换成Map
	 * @author Frodez
	 * @param <K>
	 * @param <V>
	 * @param json json字符串
	 * @date 2018-12-02
	 */
	public static Map<String, Object> map(String json) {
		try {
			return OBJECT_MAPPER.readValue(json, DEFAULT_MAP_TYPE);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将json字符串转换成Map
	 * @author Frodez
	 * @param <K>
	 * @param <V>
	 * @param json json字符串
	 * @date 2018-12-02
	 */
	public static <K, V> Map<K, V> map(String json, Class<K> k, Class<V> v) {
		Objects.requireNonNull(k);
		Objects.requireNonNull(v);
		try {
			return OBJECT_MAPPER.readValue(json, getMapJavaType(k, v));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static JavaType getMapJavaType(Class<?> k, Class<?> v) {
		return mapTypeCache.computeIfAbsent(k.getName().concat(v.getName()), (i) -> TYPE_FACTORY
			.constructParametricType(HashMap.class, k, v));
	}

	/**
	 * 将json字符串转换成List,发生异常返回null
	 * @author Frodez
	 * @param <T>
	 * @param json json字符串
	 * @date 2018-12-02
	 */
	public static <T> List<T> list(String json, Class<T> klass) {
		Objects.requireNonNull(klass);
		try {
			return OBJECT_MAPPER.readValue(json, getCollectionJavaType(ArrayList.class, klass));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将json字符串转换成Set,发生异常返回null
	 * @author Frodez
	 * @param <T>
	 * @param json json字符串
	 * @date 2018-12-02
	 */
	public static <T> Set<T> set(String json, Class<T> klass) {
		Objects.requireNonNull(klass);
		try {
			return OBJECT_MAPPER.readValue(json, getCollectionJavaType(HashSet.class, klass));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static JavaType getCollectionJavaType(Class<?> collection, Class<?> klass) {
		return collectionTypeCache.computeIfAbsent(klass, (i) -> TYPE_FACTORY.constructParametricType(collection,
			klass));
	}

}
