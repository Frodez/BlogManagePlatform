package frodez.util.common;

import frodez.constant.setting.DefaultTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.Assert;

/**
 * 日期工具类
 * @author Frodez
 * @date 2019-02-17
 */
public class DateUtil {

	private static final Map<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap<>();

	private static DateTimeFormatter getFormatter(String pattern) {
		DateTimeFormatter formatter = FORMATTER_CACHE.get(pattern);
		if (formatter != null) {
			return formatter;
		}
		formatter = DateTimeFormatter.ofPattern(pattern);
		FORMATTER_CACHE.put(pattern, formatter);
		return formatter;
	}

	/**
	 * 将date转换为yyyy-MM-dd格式字符串
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static String dateStr(Date date) {
		Assert.notNull(date, "参数不能为空!");
		return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()).toString();
	}

	/**
	 * 获取当前时间的yyyy-MM-dd格式字符串
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static String todayStr() {
		return LocalDate.now().toString();
	}

	/**
	 * 获取当前日期(时刻为0点0分)
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static Date today() {
		return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 格式化日期
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date parse(String pattern, String date) {
		Assert.notNull(pattern, "格式不能为空!");
		Assert.notNull(date, "日期不能为空!");
		return Date.from(LocalDateTime.parse(date, getFormatter(pattern)).atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 格式化日期(yyyy-MM-dd格式)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date date(String date) {
		return parse(DefaultTime.DATE_PATTERN, date);
	}

	/**
	 * 格式化时间(HH:mm:ss格式)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date time(String date) {
		return parse(DefaultTime.TIME_PATTERN, date);
	}

	/**
	 * 格式化日期(yyyy-MM-dd HH:mm:ss格式)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date dateTime(String date) {
		return parse(DefaultTime.DATE_TIME_PATTERN, date);
	}

}
