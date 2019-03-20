package frodez.util.common;

import frodez.util.constant.setting.DefTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

/**
 * 日期工具类
 * @author Frodez
 * @date 2019-02-17
 */
@UtilityClass
public class DateUtil {

	private static final ZoneOffset DEFAULT_OFFSET = OffsetDateTime.now().getOffset();

	/**
	 * yyyy-MM-dd
	 */
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DefTime.DATE_PATTERN);

	/**
	 * HH:mm:ss
	 */
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DefTime.TIME_PATTERN);

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DefTime.DATE_TIME_PATTERN);

	/**
	 * 将date转换为yyyy-MM-dd格式字符串
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static String dateStr(final Date date) {
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
		return Date.from(LocalDate.now().atStartOfDay().toInstant(DEFAULT_OFFSET));
	}

	/**
	 * 格式化日期(yyyy-MM-dd格式)
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date date(String date) {
		return Date.from(LocalDate.parse(date, DATE_FORMATTER).atStartOfDay().toInstant(DEFAULT_OFFSET));
	}

	/**
	 * 是正确的yyyy-MM-dd格式日期
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean isDate(String date) {
		Assert.notNull(date, "date must not be null");
		try {
			LocalDate.parse(date, DATE_FORMATTER);
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
		return Date.from(LocalTime.parse(date, TIME_FORMATTER).atDate(LocalDate.now()).toInstant(DEFAULT_OFFSET));
	}

	/**
	 * 是正确的HH:mm:ss格式日期
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean isTime(String date) {
		Assert.notNull(date, "date must not be null");
		try {
			LocalTime.parse(date, TIME_FORMATTER);
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
		return Date.from(LocalDateTime.parse(date, DATE_TIME_FORMATTER).atOffset(DEFAULT_OFFSET).toInstant());
	}

	/**
	 * 是正确的yyyy-MM-dd HH:mm:ss格式日期
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean isDateTime(String date) {
		Assert.notNull(date, "date must not be null");
		try {
			LocalDateTime.parse(date, DATE_TIME_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
