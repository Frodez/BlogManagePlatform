package frodez.util.common;

import frodez.util.constant.setting.DefDecimal;
import java.math.BigDecimal;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

/**
 * Decimal工具类
 * @see frodez.util.constant.setting.DefDecimal
 * @author Frodez
 * @date 2019-03-27
 */
@UtilityClass
public class DecimalUtil {

	/**
	 * 标准化
	 * @see frodez.util.constant.setting.DefDecimal#PRECISION
	 * @see frodez.util.constant.setting.DefDecimal#ROUND_MODE
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal normalize(BigDecimal decimal) {
		Assert.notNull(decimal, "decimal must not be null");
		return decimal.setScale(DefDecimal.PRECISION, DefDecimal.ROUND_MODE);
	}

	/**
	 * 标准化
	 * @see frodez.util.constant.setting.DefDecimal#PRECISION
	 * @see frodez.util.constant.setting.DefDecimal#ROUND_MODE
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal normalize(Integer value) {
		Assert.notNull(value, "value must not be null");
		return new BigDecimal(value).setScale(DefDecimal.PRECISION, DefDecimal.ROUND_MODE);
	}

	/**
	 * 标准化
	 * @see frodez.util.constant.setting.DefDecimal#PRECISION
	 * @see frodez.util.constant.setting.DefDecimal#ROUND_MODE
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal normalize(Double value) {
		Assert.notNull(value, "value must not be null");
		return new BigDecimal(value).setScale(DefDecimal.PRECISION, DefDecimal.ROUND_MODE);
	}

	/**
	 * 标准化
	 * @see frodez.util.constant.setting.DefDecimal#PRECISION
	 * @see frodez.util.constant.setting.DefDecimal#ROUND_MODE
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal normalize(Long value) {
		Assert.notNull(value, "value must not be null");
		return new BigDecimal(value).setScale(DefDecimal.PRECISION, DefDecimal.ROUND_MODE);
	}

	/**
	 * 标准化
	 * @see frodez.util.constant.setting.DefDecimal#PRECISION
	 * @see frodez.util.constant.setting.DefDecimal#ROUND_MODE
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal normalize(String value) {
		Assert.notNull(value, "value must not be null");
		return new BigDecimal(value).setScale(DefDecimal.PRECISION, DefDecimal.ROUND_MODE);
	}

	/**
	 * 批量相加并标准化,第一个数为被加数
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal add(BigDecimal first, BigDecimal... args) {
		return add(true, first, args);
	}

	/**
	 * 批量相加,第一个数为被加数
	 * @param normalized 是否进行标准化, true为进行标准化,false为不进行标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal add(boolean normalized, BigDecimal first, BigDecimal... args) {
		if (EmptyUtil.yes(args)) {
			throw new IllegalArgumentException();
		}
		BigDecimal result = first;
		for (int i = 0; i < args.length; ++i) {
			result = result.add(args[i]);
		}
		if (normalized) {
			return DecimalUtil.normalize(result);
		}
		return result;
	}

	/**
	 * 批量相减并标准化,第一个数为被减数
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal subtract(BigDecimal first, BigDecimal... args) {
		return subtract(true, first, args);
	}

	/**
	 * 批量相减,第一个数为被减数
	 * @see frodez.util.constant.setting.DefDecimal#PRECISION
	 * @see frodez.util.constant.setting.DefDecimal#ROUND_MODE
	 * @param normalized 是否进行标准化, true为进行标准化,false为不进行标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal subtract(boolean normalized, BigDecimal first, BigDecimal... args) {
		if (EmptyUtil.yes(args)) {
			throw new IllegalArgumentException();
		}
		BigDecimal result = first;
		for (int i = 0; i < args.length; ++i) {
			result = result.subtract(args[i]);
		}
		if (normalized) {
			return DecimalUtil.normalize(result);
		}
		return result;
	}

	/**
	 * 批量相乘并标准化,第一个数为被乘数
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal multiply(BigDecimal first, BigDecimal... args) {
		return multiply(true, first, args);
	}

	/**
	 * 批量相乘,第一个数为被乘数
	 * @see frodez.util.constant.setting.DefDecimal#PRECISION
	 * @see frodez.util.constant.setting.DefDecimal#ROUND_MODE
	 * @param normalized 是否进行标准化, true为进行标准化,false为不进行标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal multiply(boolean normalized, BigDecimal first, BigDecimal... args) {
		if (EmptyUtil.yes(args)) {
			throw new IllegalArgumentException();
		}
		BigDecimal result = first;
		for (int i = 0; i < args.length; ++i) {
			result = result.multiply(args[i]);
		}
		if (normalized) {
			return DecimalUtil.normalize(result);
		}
		return result;
	}

	/**
	 * 批量相除并标准化,第一个数为被除数
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal divide(BigDecimal first, BigDecimal... args) {
		return divide(true, first, args);
	}

	/**
	 * 批量相除,第一个数为被除数
	 * @see frodez.util.constant.setting.DefDecimal#PRECISION
	 * @see frodez.util.constant.setting.DefDecimal#ROUND_MODE
	 * @param normalized 是否进行标准化, true为进行标准化,false为不进行标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal divide(boolean normalized, BigDecimal first, BigDecimal... args) {
		if (EmptyUtil.yes(args)) {
			throw new IllegalArgumentException();
		}
		BigDecimal result = first;
		//按照每次计算精度减1的标准得出内部精度
		int precision = args.length + DefDecimal.PRECISION;
		for (int i = 0; i < args.length; ++i) {
			result = result.divide(args[i], precision, DefDecimal.ROUND_MODE);
		}
		if (normalized) {
			return DecimalUtil.normalize(result);
		}
		return result;
	}

}
