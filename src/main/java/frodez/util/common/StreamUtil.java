package frodez.util.common;

import frodez.util.beans.param.QueryPage;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.Assert;

@UtilityClass
public class StreamUtil {

	/**
	 * 对stream进行分页查询
	 * @author Frodez
	 * @date 2019-06-19
	 */
	public static <T> Stream<T> page(Collection<T> collection, QueryPage page) {
		Assert.notNull(page, "page must not be null");
		Assert.notNull(collection, "collection must not be null");
		return page(collection, page.getPageNum(), page.getPageSize());
	}

	/**
	 * 对stream进行分页查询
	 * @param pageNum 页码数。为了和mysql分页查询保持一致,小于等于1时,忽略该参数。
	 * @param pageSize 单页容量
	 * @author Frodez
	 * @date 2019-06-19
	 */
	public static <T> Stream<T> page(Collection<T> collection, int pageNum, int pageSize) {
		Assert.notNull(collection, "collection must not be null");
		Assert.state(pageNum >= 0, "pageNum can't be negetive");
		Assert.state(pageSize > 0, "pageSize must be positive");
		return pageNum > 1 ? collection.stream().skip((pageNum - 1) * pageSize).limit(pageSize) : collection.stream().limit(pageSize);
	}

	/**
	 * 对stream进行分页查询
	 * @author Frodez
	 * @date 2019-06-19
	 */
	public static <T> Stream<T> rowBounds(Collection<T> collection, RowBounds rowBounds) {
		Assert.notNull(rowBounds, "rowBounds must not be null");
		Assert.notNull(collection, "stream must not be null");
		return rowBounds(collection, rowBounds.getLimit(), rowBounds.getOffset());
	}

	/**
	 * 对stream进行分页查询
	 * @param limit mysql风格的limit
	 * @param offset mysql风格的offset
	 * @author Frodez
	 * @date 2019-06-19
	 */
	public static <T> Stream<T> rowBounds(Collection<T> collection, int limit, int offset) {
		Assert.notNull(collection, "stream must not be null");
		Assert.state(limit >= 0, "limit can't be negetive");
		Assert.state(offset > 0, "offset must be positive");
		return limit > 0 ? collection.stream().skip(offset).limit(limit) : collection.stream().limit(limit);
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T, R> List<R> list(Collection<T> collection, Function<T, R> function) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(function, "function must not be null");
		return collection.stream().map(function).collect(Collectors.toList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T, R> List<R> list(T[] collection, Function<T, R> function) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(function, "function must not be null");
		return Stream.of(collection).map(function).collect(Collectors.toList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T> List<T> filterList(Collection<T> collection, Predicate<T> predicate) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(predicate, "predicate must not be null");
		return collection.stream().filter(predicate).collect(Collectors.toList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T> List<T> filterList(T[] collection, Predicate<T> predicate) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(predicate, "predicate must not be null");
		return Stream.of(collection).filter(predicate).collect(Collectors.toList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T, R> List<R> unmodifiableList(Collection<T> collection, Function<T, R> function) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(function, "function must not be null");
		return collection.stream().map(function).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T, R> List<R> unmodifiableList(T[] collection, Function<T, R> function) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(function, "function must not be null");
		return Stream.of(collection).map(function).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T> List<T> filterUnmodifiableList(Collection<T> collection, Predicate<T> predicate) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(predicate, "predicate must not be null");
		return collection.stream().filter(predicate).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * 遍历collection生成新的list
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T> List<T> filterUnmodifiableList(T[] collection, Predicate<T> predicate) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(predicate, "predicate must not be null");
		return Stream.of(collection).filter(predicate).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T, R> Set<R> set(Collection<T> collection, Function<T, R> function) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(function, "function must not be null");
		return collection.stream().map(function).collect(Collectors.toSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T, R> Set<R> set(T[] collection, Function<T, R> function) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(function, "function must not be null");
		return Stream.of(collection).map(function).collect(Collectors.toSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T> Set<T> filterSet(Collection<T> collection, Predicate<T> predicate) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(predicate, "predicate must not be null");
		return collection.stream().filter(predicate).collect(Collectors.toSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T> Set<T> filterSet(T[] collection, Predicate<T> predicate) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(predicate, "predicate must not be null");
		return Stream.of(collection).filter(predicate).collect(Collectors.toSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T, R> Set<R> unmodifiableSet(Collection<T> collection, Function<T, R> function) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(function, "function must not be null");
		return collection.stream().map(function).collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T, R> Set<R> unmodifiableSet(T[] collection, Function<T, R> function) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(function, "function must not be null");
		return Stream.of(collection).map(function).collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T> Set<T> filterUnmodifiableSet(Collection<T> collection, Predicate<T> predicate) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(predicate, "predicate must not be null");
		return collection.stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 * @date 2019-12-09
	 */
	public static <T> Set<T> filterUnmodifiableSet(T[] collection, Predicate<T> predicate) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(predicate, "predicate must not be null");
		return Stream.of(collection).filter(predicate).collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 * @param <T>
	 * @param <K>
	 * @param <V>
	 * @date 2019-12-09
	 */
	public static <T, K, V> HashMap<K, V> hashMap(Collection<T> collection, Function<T, K> keyMapper, Function<T, V> valueMapper) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(keyMapper, "keyMapper must not be null");
		Assert.notNull(valueMapper, "valueMapper must not be null");
		return (HashMap<K, V>) collection.stream().collect(Collectors.toMap(keyMapper, valueMapper));
	}

	/**
	 * 遍历collection生成新的set
	 * @author Frodez
	 * @param <T>
	 * @param <K>
	 * @param <V>
	 * @date 2019-12-09
	 */
	public static <T, K, V> HashMap<K, V> hashMap(Collection<T> collection, Function<T, K> keyMapper, Function<T, V> valueMapper, BinaryOperator<
		V> mergeFunction) {
		Assert.notNull(collection, "collection must not be null");
		Assert.notNull(keyMapper, "keyMapper must not be null");
		Assert.notNull(valueMapper, "valueMapper must not be null");
		Assert.notNull(mergeFunction, "mergeFunction must not be null");
		return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction, HashMap::new));
	}

	/**
	 * HashMap stream collector
	 * @author Frodez
	 * @param <M>
	 * @date 2019-12-09
	 */
	public static class MapBuilder<T, K, V> {

		Function<T, K> keyMapper;

		Function<T, V> valueMapper;

		BinaryOperator<V> mergeFunction;

		private MapBuilder() {
		}

		public static <T, K, V> MapBuilder<T, K, V> instance() {
			return new MapBuilder<>();
		}

		/**
		 * 配置keyMapper
		 * @author Frodez
		 * @date 2019-12-09
		 */
		public MapBuilder<T, K, V> key(Function<T, K> keyMapper) {
			if (this.keyMapper != null) {
				throw new IllegalStateException();
			}
			this.keyMapper = keyMapper;
			return this;
		}

		/**
		 * 配置valueMapper
		 * @author Frodez
		 * @date 2019-12-09
		 */
		public MapBuilder<T, K, V> value(Function<T, V> valueMapper) {
			if (this.valueMapper != null) {
				throw new IllegalStateException();
			}
			this.valueMapper = valueMapper;
			return this;
		}

		/**
		 * 配置mergeFunction
		 * @author Frodez
		 * @date 2019-12-09
		 */
		public MapBuilder<T, K, V> merge(BinaryOperator<V> mergeFunction) {
			if (this.mergeFunction != null) {
				throw new IllegalStateException();
			}
			this.mergeFunction = mergeFunction;
			return this;
		}

		@SuppressWarnings("unchecked")
		private BinaryOperator<V> mergeFunction() {
			if (mergeFunction == null) {
				return (m1, m2) -> {
					for (Map.Entry<K, V> e : ((Map<K, V>) m2).entrySet()) {
						K k = e.getKey();
						V v = Objects.requireNonNull(e.getValue());
						V u = ((Map<K, V>) m1).putIfAbsent(k, v);
						if (u != null) {
							throw new IllegalStateException(String.format("Duplicate key %s (attempted merging values %s and %s)", k, u, v));
						}
					}
					return m1;
				};
			}
			return mergeFunction;
		}

		/**
		 * 返回collector
		 * @author Frodez
		 * @date 2019-12-09
		 */
		public Collector<T, ?, Map<K, V>> hashMap() {
			Assert.notNull(keyMapper, "keyMapper must not be null");
			Assert.notNull(valueMapper, "valueMapper must not be null");
			return Collectors.toMap(keyMapper, valueMapper, mergeFunction(), HashMap::new);
		}

		/**
		 * 返回collector
		 * @author Frodez
		 * @date 2019-12-09
		 */
		public Collector<T, ?, ConcurrentHashMap<K, V>> concurrentHashMap() {
			Assert.notNull(keyMapper, "keyMapper must not be null");
			Assert.notNull(valueMapper, "valueMapper must not be null");
			return Collectors.toConcurrentMap(keyMapper, valueMapper, mergeFunction(), ConcurrentHashMap::new);
		}

		/**
		 * 返回collector
		 * @author Frodez
		 * @date 2019-12-09
		 */
		public Collector<T, ?, Map<K, V>> unmodifiableMap() {
			Assert.notNull(keyMapper, "keyMapper must not be null");
			Assert.notNull(valueMapper, "valueMapper must not be null");
			return Collectors.toUnmodifiableMap(keyMapper, valueMapper, mergeFunction());
		}

	}

	public static void main(String[] args) {
		var a = List.of(1, 3, 5, 66, 71, 34, 20, 130, 524, 623);
		System.out.println(a.stream().collect(Collectors.toMap((item) -> item, (item) -> item)).toString());
		var builderA = MapBuilder.<Integer, Integer, Integer>instance();
		builderA.key((item) -> item);
		builderA.value((item) -> item);
		var collectorA = builderA.hashMap();
		System.out.println(a.stream().collect(collectorA).toString());
		collectorA = MapBuilder.<Integer, Integer, Integer>instance().key((item) -> item).value((item) -> item).merge((m1, m2) -> m1).hashMap();
		System.out.println(a.stream().collect(collectorA).toString());
		System.out.println("\n");

		var stream = List.of(1, 3, 5, 66, 71, 34, 20, 130, 524, 623);

		QueryPage queryPage = new QueryPage(2, 4);
		RowBounds rowBounds = new RowBounds(1, 8);
		StreamUtil.page(stream, 0, 3).forEach(System.out::println);
		System.out.println("\n");
		StreamUtil.page(stream, queryPage).forEach(System.out::println);
		System.out.println("\n");
		StreamUtil.rowBounds(stream, 3, 5).forEach(System.out::println);
		System.out.println("\n");
		StreamUtil.rowBounds(stream, rowBounds).forEach(System.out::println);
		System.out.println("\n");
		StreamUtil.rowBounds(stream, queryPage.toRowBounds()).forEach(System.out::println);
		var Random = new Random();
		var test = List.of("222", "32d", "32d", "32d", "32d", "32d", "32d", "32d", "32d", "32d", "32d").stream().map((item) -> {
			if (Random.nextBoolean()) {
				return item;
			} else {
				return null;
			}
		}).collect(Collectors.toList());
		System.out.println(String.join(";\n", test));
	}

}
