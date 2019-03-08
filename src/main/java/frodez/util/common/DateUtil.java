package frodez.util.common;

import frodez.constant.setting.DefTime;
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

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DefTime.DATE_PATTERN);

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DefTime.TIME_PATTERN);

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DefTime.DATE_TIME_PATTERN);

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
	public static String dateStr(final Date date) {
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
	 * 格式化日期<br>
	 * <strong>对于yyyy-MM-dd格式,HH:mm:ss格式和yyyy-MM-dd HH:mm:ss格式,请使用下面的专用方法!!!</strong>
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date parse(String pattern, String date) {
		Assert.notNull(pattern, "格式不能为空!");
		Assert.notNull(date, "日期不能为空!");
		return Date.from(LocalDateTime.parse(date, getFormatter(pattern)).atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 是正确的格式与符合格式的日期
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean isDate(String pattern, String date) {
		Assert.notNull(pattern, "格式不能为空!");
		Assert.notNull(date, "日期不能为空!");
		try {
			LocalDateTime.parse(date, getFormatter(pattern));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 格式化日期(yyyy-MM-dd格式)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date date(String date) {
		Assert.notNull(date, "日期不能为空!");
		return Date.from(LocalDateTime.parse(date, DATE_FORMATTER).atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 是正确的yyyy-MM-dd格式日期
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean isDate(String date) {
		Assert.notNull(date, "日期不能为空!");
		try {
			LocalDateTime.parse(date, DATE_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 格式化时间(HH:mm:ss格式)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date time(String date) {
		Assert.notNull(date, "日期不能为空!");
		return Date.from(LocalDateTime.parse(date, TIME_FORMATTER).atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 是正确的HH:mm:ss格式日期
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean isTime(String date) {
		Assert.notNull(date, "日期不能为空!");
		try {
			LocalDateTime.parse(date, TIME_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 格式化日期(yyyy-MM-dd HH:mm:ss格式)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date dateTime(String date) {
		Assert.notNull(date, "日期不能为空!");
		return Date.from(LocalDateTime.parse(date, DATE_TIME_FORMATTER).atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 是正确的yyyy-MM-dd HH:mm:ss格式日期
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean isDateTime(String date) {
		Assert.notNull(date, "日期不能为空!");
		try {
			LocalDateTime.parse(date, DATE_TIME_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
