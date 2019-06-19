package frodez.util.common;

import frodez.util.beans.param.QueryPage;
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
	public static <T> Stream<T> startPage(Stream<T> stream, QueryPage page) {
		Assert.notNull(page, "page must not be null");
		Assert.notNull(stream, "stream must not be null");
		return startPage(stream, page.getPageNum(), page.getPageSize());
	}

	/**
	 * 对stream进行分页查询
	 * @param pageNum 页码数。为了和mysql分页查询保持一致,小于等于1时,忽略该参数。
	 * @param pageSize 单页容量
	 * @author Frodez
	 * @date 2019-06-19
	 */
	public static <T> Stream<T> startPage(Stream<T> stream, int pageNum, int pageSize) {
		Assert.notNull(stream, "stream must not be null");
		Assert.state(pageNum >= 0, "limit can't be negetive");
		Assert.state(pageSize > 0, "offset must be positive");
		return pageNum > 1 ? stream.skip((pageNum - 1) * pageSize).limit(pageNum * pageSize) : stream.limit(pageSize);
	}

	/**
	 * 对stream进行分页查询
	 * @author Frodez
	 * @date 2019-06-19
	 */
	public static <T> Stream<T> startRowBounds(Stream<T> stream, RowBounds rowBounds) {
		Assert.notNull(rowBounds, "rowBounds must not be null");
		Assert.notNull(stream, "stream must not be null");
		return startPage(stream, rowBounds.getLimit(), rowBounds.getOffset());
	}

	/**
	 * 对stream进行分页查询
	 * @param limit mysql风格的limit
	 * @param offset mysql风格的offset
	 * @author Frodez
	 * @date 2019-06-19
	 */
	public static <T> Stream<T> startRowBounds(Stream<T> stream, int limit, int offset) {
		Assert.notNull(stream, "stream must not be null");
		Assert.state(limit >= 0, "limit can't be negetive");
		Assert.state(offset > 0, "offset must be positive");
		return limit > 0 ? stream.skip(limit).limit(limit + offset) : stream.limit(offset);
	}

}
