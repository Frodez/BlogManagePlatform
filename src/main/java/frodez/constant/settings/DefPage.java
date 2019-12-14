package frodez.constant.settings;

import frodez.util.beans.param.QueryPage;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.session.RowBounds;

/**
 * 默认分页参数设置
 * @author Frodez
 * @date 2019-03-06
 */
@UtilityClass
public class DefPage {

	/**
	 * 默认页码数
	 */
	public static final int NUM = 0;

	/**
	 * 默认单页容量
	 */
	public static final int SIZE = 20;

	/**
	 * 默认最大单页容量
	 */
	public static final int MAX_SIZE = 1000;

	/**
	 * 默认RowBounds
	 */
	public static final RowBounds ROW_BOUNDS = new RowBounds(NUM, SIZE);

	/**
	 * 默认QueryPage<br>
	 * 等同于 new QueryPage();<br>
	 */
	public static final QueryPage QUERY_PAGE = new QueryPage(NUM, SIZE);

}
