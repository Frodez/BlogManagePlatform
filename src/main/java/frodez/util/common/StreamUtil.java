package frodez.util.common;

import frodez.util.beans.param.QueryPage;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Function;
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
	 * HashMap stream collector
	 * @author Frodez
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

		public Collector<T, ?, Map<K, V>> hashMap() {
			Assert.notNull(keyMapper, "keyMapper must not be null");
			Assert.notNull(valueMapper, "valueMapper must not be null");
			if (mergeFunction == null) {
				return Collectors.toMap(keyMapper, valueMapper);
			} else {
				return Collectors.toMap(keyMapper, valueMapper, mergeFunction);
			}
		}

	}

	public static void main(String[] args) {
		var a = List.of(1, 3, 5, 66, 71, 34, 20, 130, 524, 623);
		System.out.println(a.stream().collect(Collectors.toMap((item) -> item, (item) -> item)).toString());
		MapBuilder<Integer, Integer, Integer> builder = MapBuilder.<Integer, Integer, Integer>instance();
		builder.key((item) -> item);
		builder.value((item) -> item);
		System.out.println(a.stream().collect(builder.hashMap()).toString());
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
