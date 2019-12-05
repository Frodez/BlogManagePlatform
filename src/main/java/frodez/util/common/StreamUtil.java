package frodez.util.common;

import frodez.util.beans.param.QueryPage;
import java.util.Collection;
import java.util.List;
import java.util.Random;
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
	public static <T> Stream<T> startPage(Collection<T> collection, QueryPage page) {
		Assert.notNull(page, "page must not be null");
		Assert.notNull(collection, "collection must not be null");
		return startPage(collection, page.getPageNum(), page.getPageSize());
	}

	/**
	 * 对stream进行分页查询
	 * @param pageNum 页码数。为了和mysql分页查询保持一致,小于等于1时,忽略该参数。
	 * @param pageSize 单页容量
	 * @author Frodez
	 * @date 2019-06-19
	 */
	public static <T> Stream<T> startPage(Collection<T> collection, int pageNum, int pageSize) {
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
	public static <T> Stream<T> startRowBounds(Collection<T> collection, RowBounds rowBounds) {
		Assert.notNull(rowBounds, "rowBounds must not be null");
		Assert.notNull(collection, "stream must not be null");
		return startRowBounds(collection, rowBounds.getLimit(), rowBounds.getOffset());
	}

	/**
	 * 对stream进行分页查询
	 * @param limit mysql风格的limit
	 * @param offset mysql风格的offset
	 * @author Frodez
	 * @date 2019-06-19
	 */
	public static <T> Stream<T> startRowBounds(Collection<T> collection, int limit, int offset) {
		Assert.notNull(collection, "stream must not be null");
		Assert.state(limit >= 0, "limit can't be negetive");
		Assert.state(offset > 0, "offset must be positive");
		return limit > 0 ? collection.stream().skip(offset).limit(limit) : collection.stream().limit(limit);
	}

	public static void main(String[] args) {
		var stream = List.of(1, 3, 5, 66, 71, 34, 20, 130, 524, 623);
		QueryPage queryPage = new QueryPage(2, 4);
		RowBounds rowBounds = new RowBounds(1, 8);
		StreamUtil.startPage(stream, 0, 3).forEach(System.out::println);
		System.out.println("\n");
		StreamUtil.startPage(stream, queryPage).forEach(System.out::println);
		System.out.println("\n");
		StreamUtil.startRowBounds(stream, 3, 5).forEach(System.out::println);
		System.out.println("\n");
		StreamUtil.startRowBounds(stream, rowBounds).forEach(System.out::println);
		System.out.println("\n");
		StreamUtil.startRowBounds(stream, queryPage.toRowBounds()).forEach(System.out::println);
		var random = new Random();
		var test = List.of("222", "32d", "32d", "32d", "32d", "32d", "32d", "32d", "32d", "32d", "32d").stream().map((item) -> {
			if (random.nextBoolean()) {
				return item;
			} else {
				return null;
			}
		}).collect(Collectors.toList());
		System.out.println(String.join(";\n", test));
	}

}
