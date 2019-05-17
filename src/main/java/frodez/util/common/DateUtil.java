package frodez.util.common;

import frodez.constant.settings.DefTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

/**
 * 日期工具类<br>
 * 包括了对符合默认格式的日期,时间和日期——时间的处理。
 * @see frodez.constant.settings.DefTime
 * @author Frodez
 * @date 2019-02-17
 */
@UtilityClass
public class DateUtil {

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
		return Date.from(LocalDate.now().atStartOfDay().toInstant(DefTime.DEFAULT_OFFSET));
	}

	/**
	 * 格式化日期(yyyy-MM-dd格式)
	 * @see frodez.constant.settings.DefTime#DATE_PATTERN
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date date(String date) {
		return Date.from(LocalDate.parse(date, DefTime.DATE_FORMATTER).atStartOfDay().toInstant(
			DefTime.DEFAULT_OFFSET));
	}

	/**
	 * 是正确的yyyy-MM-dd格式日期
	 * @see frodez.constant.settings.DefTime#DATE_PATTERN
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean isDate(String date) {
		Assert.notNull(date, "date must not be null");
		try {
			LocalDate.parse(date, DefTime.DATE_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 格式化时间(HH:mm:ss格式)
	 * @see frodez.constant.settings.DefTime#TIME_PATTERN
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date time(String date) {
		return Date.from(LocalTime.parse(date, DefTime.TIME_FORMATTER).atDate(LocalDate.now()).toInstant(
			DefTime.DEFAULT_OFFSET));
	}

	/**
	 * 是正确的HH:mm:ss格式日期
	 * @see frodez.constant.settings.DefTime#TIME_PATTERN
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean isTime(String date) {
		Assert.notNull(date, "date must not be null");
		try {
			LocalTime.parse(date, DefTime.TIME_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 格式化日期(yyyy-MM-dd HH:mm:ss格式)
	 * @see frodez.constant.settings.DefTime#DATE_TIME_PATTERN
	 * @author Frodez
	 * @date 2019-02-17
	 */
	public static Date dateTime(String date) {
		return Date.from(LocalDateTime.parse(date, DefTime.DATE_TIME_FORMATTER).atOffset(DefTime.DEFAULT_OFFSET)
			.toInstant());
	}

	/**
	 * 是正确的yyyy-MM-dd HH:mm:ss格式日期
	 * @see frodez.constant.settings.DefTime#DATE_TIME_PATTERN
	 * @author Frodez
	 * @date 2019-03-01
	 */
	public static boolean isDateTime(String date) {
		Assert.notNull(date, "date must not be null");
		try {
			LocalDateTime.parse(date, DefTime.DATE_TIME_FORMATTER);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
