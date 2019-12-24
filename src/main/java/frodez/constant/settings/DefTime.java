package frodez.constant.settings;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;

/**
 * 默认时间设置
 * @author Frodez
 * @date 2019-03-27
 */
@UtilityClass
public class DefTime {

	/**
	 * 默认时间差
	 */
	public static final ZoneOffset DEFAULT_OFFSET = OffsetDateTime.now().getOffset();

	/**
	 * 默认时间单位(毫秒)
	 */
	public static final TimeUnit UNIT = TimeUnit.MILLISECONDS;

	/**
	 * 默认日期格式
	 */
	public static final String DATE_PATTERN = "yyyy-MM-dd";

	/**
	 * 默认时间格式
	 */
	public static final String TIME_PATTERN = "HH:mm:ss";

	/**
	 * 默认日期——时间格式
	 */
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 默认精细时间格式
	 */
	public static final String PRECISIVE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 默认日期类型格式化器
	 */
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DefTime.DATE_PATTERN);

	/**
	 * 默认时间类型格式化器
	 */
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DefTime.TIME_PATTERN);

	/**
	 * 默认日期——时间类型格式化器
	 */
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DefTime.DATE_TIME_PATTERN);

	/**
	 * 默认精细时间类型格式化器
	 */
	public static final DateTimeFormatter PRECISIVE_FORMATTER = DateTimeFormatter.ofPattern(DefTime.PRECISIVE_PATTERN);

}
