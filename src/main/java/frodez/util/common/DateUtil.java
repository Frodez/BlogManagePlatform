package frodez.util.common;

import java.util.Date;
import org.joda.time.LocalDate;
import org.springframework.util.Assert;

public class DateUtil {

	/**
	 * 将date转换为yyyy-MM-dd格式字符串
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static String dateStr(Date date) {
		Assert.notNull(date, "参数不能为空!");
		return new LocalDate(date).toString();
	}

	/**
	 * 获取当前时间的yyyy-MM-dd格式字符串
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static String today() {
		return LocalDate.now().toString();
	}

}
