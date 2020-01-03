package frodez.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import frodez.util.common.EmptyUtil;
import frodez.util.common.StrUtil;
import frodez.util.spring.ContextUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * json工具类<br>
 * 本工具类使用的ObjectMapper是jackson的springboot-starter所自动配置得到的,不会使得项目中引入两个不同的ObjectMapper。<br>
 * 本工具类对html字符的转义做了专门处理。<br>
 * 本工具类大量采用缓存,可较大程度上提高速度。<br>
 * @author Frodez
 * @date 2018-11-27
 */
@Component("jsonUtil")
@DependsOn("contextUtil")
public class JSONUtil {

	private static ObjectMapper OBJECT_MAPPER;

	@SuppressWarnings("rawtypes")
	private static Class<HashMap> DEFAULT_MAP_CLASS = HashMap.class;

	@SuppressWarnings("rawtypes")
	private static Class<ArrayList> DEFAULT_LIST_CLASS = ArrayList.class;

	@SuppressWarnings("rawtypes")
	private static Class<HashSet> DEFAULT_SET_CLASS = HashSet.class;

	private static String DEFAULT_MAP_CLASS_NAME = DEFAULT_MAP_CLASS.getName();

	private static String DEFAULT_LIST_CLASS_NAME = DEFAULT_LIST_CLASS.getName();

	private static String DEFAULT_SET_CLASS_NAME = DEFAULT_SET_CLASS.getName();

	private static ObjectReader DEFAULT_MAP_READER;

	private static Map<Class<?>, ObjectReader> singleClassReaderCache = new ConcurrentHashMap<>();

	private static Map<Type, ObjectReader> singleTypeReaderCache = new ConcurrentHashMap<>();

	private static Map<String, ObjectReader> multiClassReaderCache = new ConcurrentHashMap<>();

	private static Map<Class<?>, ObjectWriter> writerCache = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		OBJECT_MAPPER = ContextUtil.bean(ObjectMapper.class);
		DEFAULT_MAP_READER = OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructParametricType(DEFAULT_MAP_CLASS, String.class,
			Object.class));
		Assert.notNull(OBJECT_MAPPER, "OBJECT_MAPPER must not be null");
		Assert.notNull(DEFAULT_MAP_READER, "DEFAULT_MAP_READER must not be null");
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
	 * @throws JsonProcessingException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static String string(Object object) {
		Assert.notNull(object, "object must not be null");
		return writerCache.computeIfAbsent(object.getClass(), (o) -> OBJECT_MAPPER.writerFor(object.getClass())).writeValueAsString(object);
	}

	/**
	 * 获取对象对应的ObjectWriter
	 * @author Frodez
	 * @param object 对象
	 * @throws JsonProcessingException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static ObjectWriter writer(Object object) {
		Assert.notNull(object, "object must not be null");
		return writerCache.computeIfAbsent(object.getClass(), (o) -> OBJECT_MAPPER.writerFor(object.getClass()));
	}

	/**
	 * 获取Reader
	 * @author Frodez
	 * @date 2019-05-24
	 */
	@SneakyThrows
	public static ObjectReader reader(Class<?> parametrized, @Nullable Class<?>... genericClasses) {
		Assert.notNull(parametrized, "parametrized must not be null");
		if (EmptyUtil.yes(genericClasses)) {
			return singleClassReaderCache.computeIfAbsent(parametrized, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(
				parametrized)));
		} else {
			StringBuilder builder = new StringBuilder(parametrized.getName());
			for (Class<?> klass : genericClasses) {
				builder.append(klass.getName());
			}
			return multiClassReaderCache.computeIfAbsent(builder.toString(), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory()
				.constructParametricType(parametrized, genericClasses)));
		}
	}

	/**
	 * 将InputStream的数据转换成Map
	 * @author Frodez
	 * @param <K>
	 * @param <V>
	 * @param json json字符串
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static Map<String, Object> map(InputStream stream) {
		Assert.notNull(stream, "stream must not be null");
		return DEFAULT_MAP_READER.readValue(stream);
	}

	/**
	 * 将json字符串转换成Map
	 * @author Frodez
	 * @param <K>
	 * @param <V>
	 * @param json json字符串
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static Map<String, Object> map(String json) {
		Assert.notNull(json, "json must not be null");
		return DEFAULT_MAP_READER.readValue(json);
	}

	/**
	 * 将InputStream的数据转换成Map
	 * @author Frodez
	 * @param <K>
	 * @param <V>
	 * @param json json字符串
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static <K, V> Map<K, V> map(InputStream stream, Class<K> k, Class<V> v) {
		Assert.notNull(stream, "stream must not be null");
		Assert.notNull(k, "k must not be null");
		Assert.notNull(v, "v must not be null");
		return multiClassReaderCache.computeIfAbsent(StrUtil.concat(DEFAULT_MAP_CLASS_NAME, k.getName(), v.getName()), (i) -> OBJECT_MAPPER.readerFor(
			OBJECT_MAPPER.getTypeFactory().constructParametricType(DEFAULT_MAP_CLASS, k, v))).readValue(stream);
	}

	/**
	 * 将json字符串转换成Map
	 * @author Frodez
	 * @param <K>
	 * @param <V>
	 * @param json json字符串
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static <K, V> Map<K, V> map(String json, Class<K> k, Class<V> v) {
		Assert.notNull(json, "json must not be null");
		Assert.notNull(k, "k must not be null");
		Assert.notNull(v, "v must not be null");
		return multiClassReaderCache.computeIfAbsent(StrUtil.concat(DEFAULT_MAP_CLASS_NAME, k.getName(), v.getName()), (i) -> OBJECT_MAPPER.readerFor(
			OBJECT_MAPPER.getTypeFactory().constructParametricType(DEFAULT_MAP_CLASS, k, v))).readValue(json);
	}

	/**
	 * 将InputStream的数据转换成List
	 * @author Frodez
	 * @param <T>
	 * @param json json字符串
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static <T> List<T> list(InputStream stream, Class<T> klass) {
		Assert.notNull(stream, "stream must not be null");
		Assert.notNull(stream, "stream must not be null");
		return multiClassReaderCache.computeIfAbsent(StrUtil.concat(DEFAULT_LIST_CLASS_NAME, klass.getName()), (i) -> OBJECT_MAPPER.readerFor(
			OBJECT_MAPPER.getTypeFactory().constructParametricType(DEFAULT_LIST_CLASS, klass))).readValue(stream);
	}

	/**
	 * 将json字符串转换成List
	 * @author Frodez
	 * @param <T>
	 * @param json json字符串
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static <T> List<T> list(String json, Class<T> klass) {
		Assert.notNull(json, "json must not be null");
		Assert.notNull(klass, "klass must not be null");
		return multiClassReaderCache.computeIfAbsent(StrUtil.concat(DEFAULT_LIST_CLASS_NAME, klass.getName()), (i) -> OBJECT_MAPPER.readerFor(
			OBJECT_MAPPER.getTypeFactory().constructParametricType(DEFAULT_LIST_CLASS, klass))).readValue(json);
	}

	/**
	 * 将InputStream的数据转换成Set
	 * @author Frodez
	 * @param <T>
	 * @param json json字符串
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static <T> Set<T> set(InputStream stream, Class<T> klass) {
		Assert.notNull(stream, "stream must not be null");
		Assert.notNull(klass, "klass must not be null");
		return multiClassReaderCache.computeIfAbsent(StrUtil.concat(DEFAULT_SET_CLASS_NAME, klass.getName()), (i) -> OBJECT_MAPPER.readerFor(
			OBJECT_MAPPER.getTypeFactory().constructParametricType(DEFAULT_SET_CLASS, klass))).readValue(stream);
	}

	/**
	 * 将json字符串转换成Set
	 * @author Frodez
	 * @param <T>
	 * @param json json字符串
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static <T> Set<T> set(String json, Class<T> klass) {
		Assert.notNull(json, "json must not be null");
		Assert.notNull(klass, "klass must not be null");
		return multiClassReaderCache.computeIfAbsent(StrUtil.concat(DEFAULT_SET_CLASS_NAME, klass.getName()), (i) -> OBJECT_MAPPER.readerFor(
			OBJECT_MAPPER.getTypeFactory().constructParametricType(DEFAULT_SET_CLASS, klass))).readValue(json);
	}

	/**
	 * 将InputStream的数据转换成对象
	 * @author Frodez
	 * @param json json字符串
	 * @param klass 对象类型
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static <T> T as(InputStream stream, Class<T> klass) {
		Assert.notNull(stream, "stream must not be null");
		Assert.notNull(klass, "klass must not be null");
		return singleClassReaderCache.computeIfAbsent(klass, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(klass)))
			.readValue(stream);
	}

	/**
	 * 将json字符串转换成对象
	 * @author Frodez
	 * @param json json字符串
	 * @param klass 对象类型
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static <T> T as(String json, Class<T> klass) {
		Assert.notNull(json, "json must not be null");
		Assert.notNull(klass, "klass must not be null");
		return singleClassReaderCache.computeIfAbsent(klass, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(klass)))
			.readValue(json);
	}

	/**
	 * 将InputStream的数据转换成对象
	 * @author Frodez
	 * @param json json字符串
	 * @param klass 对象类型
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static <T> T as(InputStream stream, Type type) {
		Assert.notNull(stream, "stream must not be null");
		Assert.notNull(type, "type must not be null");
		return singleTypeReaderCache.computeIfAbsent(type, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(type)))
			.readValue(stream);
	}

	/**
	 * 将json字符串转换成对象
	 * @author Frodez
	 * @param json json字符串
	 * @param klass 对象类型
	 * @throws IOException
	 * @date 2018-12-02
	 */
	@SneakyThrows
	public static <T> T as(String json, Type type) {
		Assert.notNull(json, "json must not be null");
		Assert.notNull(type, "type must not be null");
		return singleTypeReaderCache.computeIfAbsent(type, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(type)))
			.readValue(json);
	}

	/**
	 * 将InputStream的数据转换成任意类型
	 * @param parametrized 原型
	 * @param genericClasses 泛型,按从外到内的顺序从前往后排列
	 * @author Frodez
	 * @throws IOException
	 * @date 2019-03-13
	 */
	@SneakyThrows
	public static <T> T as(InputStream stream, Class<T> parametrized, @Nullable Class<?>... genericClasses) {
		Assert.notNull(stream, "stream must not be null");
		Assert.notNull(parametrized, "parametrized must not be null");
		if (EmptyUtil.yes(genericClasses)) {
			return singleClassReaderCache.computeIfAbsent(parametrized, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(
				parametrized))).readValue(stream);
		} else {
			StringBuilder builder = new StringBuilder(parametrized.getName());
			for (Class<?> klass : genericClasses) {
				builder.append(klass.getName());
			}
			return multiClassReaderCache.computeIfAbsent(builder.toString(), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory()
				.constructParametricType(parametrized, genericClasses))).readValue(stream);
		}
	}

	/**
	 * 将json字符串转换成任意类型
	 * @param parametrized 原型
	 * @param genericClasses 泛型,按从外到内的顺序从前往后排列
	 * @author Frodez
	 * @throws IOException
	 * @date 2019-03-13
	 */
	@SneakyThrows
	public static <T> T as(String json, Class<T> parametrized, @Nullable Class<?>... genericClasses) {
		Assert.notNull(json, "json must not be null");
		Assert.notNull(parametrized, "parametrized must not be null");
		if (EmptyUtil.yes(genericClasses)) {
			return singleClassReaderCache.computeIfAbsent(parametrized, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(
				parametrized))).readValue(json);
		} else {
			StringBuilder builder = new StringBuilder(parametrized.getName());
			for (Class<?> klass : genericClasses) {
				builder.append(klass.getName());
			}
			return multiClassReaderCache.computeIfAbsent(builder.toString(), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory()
				.constructParametricType(parametrized, genericClasses))).readValue(json);
		}
	}

	/**
	 * 将json字符串转换成任意类型(类型已擦除)
	 * @param parametrized 原型
	 * @param genericClasses 泛型,按从外到内的顺序从前往后排列
	 * @author Frodez
	 * @throws IOException
	 * @date 2019-03-13
	 */
	@SneakyThrows
	public static Object object(String json, Class<?> parametrized, @Nullable Class<?>... genericClasses) {
		Assert.notNull(json, "json must not be null");
		Assert.notNull(parametrized, "parametrized must not be null");
		if (EmptyUtil.yes(genericClasses)) {
			return singleClassReaderCache.computeIfAbsent(parametrized, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(
				parametrized))).readValue(json);
		} else {
			StringBuilder builder = new StringBuilder(parametrized.getName());
			for (Class<?> klass : genericClasses) {
				builder.append(klass.getName());
			}
			return multiClassReaderCache.computeIfAbsent(builder.toString(), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory()
				.constructParametricType(parametrized, genericClasses))).readValue(json);
		}
	}

	/**
	 * 将InputStream的数据转换成任意类型(类型已擦除)
	 * @param parametrized 原型
	 * @param genericClasses 泛型,按从外到内的顺序从前往后排列
	 * @author Frodez
	 * @throws IOException
	 * @date 2019-03-13
	 */
	@SneakyThrows
	public static Object object(InputStream stream, Class<?> parametrized, @Nullable Class<?>... genericClasses) {
		Assert.notNull(stream, "stream must not be null");
		Assert.notNull(parametrized, "parametrized must not be null");
		if (EmptyUtil.yes(genericClasses)) {
			return singleClassReaderCache.computeIfAbsent(parametrized, (k) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory().constructType(
				parametrized))).readValue(stream);
		} else {
			StringBuilder builder = new StringBuilder(parametrized.getName());
			for (Class<?> klass : genericClasses) {
				builder.append(klass.getName());
			}
			return multiClassReaderCache.computeIfAbsent(builder.toString(), (i) -> OBJECT_MAPPER.readerFor(OBJECT_MAPPER.getTypeFactory()
				.constructParametricType(parametrized, genericClasses))).readValue(stream);
		}
	}

}
