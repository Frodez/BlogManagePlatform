package frodez.util.common;

import frodez.constant.setting.DefaultDecimal;
import java.math.BigDecimal;
import org.springframework.util.Assert;

public class DecimalUtil {

	/**
	 * 标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal normalize(BigDecimal decimal) {
		Assert.notNull(decimal, "参数不能为空!");
		return decimal.setScale(DefaultDecimal.PRECISION, DefaultDecimal.ROUND_MODE);
	}

	/**
	 * 标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal normalize(Integer value) {
		Assert.notNull(value, "参数不能为空!");
		return new BigDecimal(value).setScale(DefaultDecimal.PRECISION, DefaultDecimal.ROUND_MODE);
	}

	/**
	 * 标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal normalize(Double value) {
		Assert.notNull(value, "参数不能为空!");
		return new BigDecimal(value).setScale(DefaultDecimal.PRECISION, DefaultDecimal.ROUND_MODE);
	}

	/**
	 * 标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal normalize(Long value) {
		Assert.notNull(value, "参数不能为空!");
		return new BigDecimal(value).setScale(DefaultDecimal.PRECISION, DefaultDecimal.ROUND_MODE);
	}

	/**
	 * 标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal normalize(String value) {
		Assert.notNull(value, "参数不能为空!");
		return new BigDecimal(value).setScale(DefaultDecimal.PRECISION, DefaultDecimal.ROUND_MODE);
	}

	/**
	 * 批量相加
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal add(BigDecimal... args) {
		Assert.notEmpty(args, "参数不能为空!");
		BigDecimal result = args[0];
		for (int i = 1; i < args.length; i++) {
			result = result.add(args[i]);
		}
		return result;
	}

	/**
	 * 批量相减,第一个数为被减数
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal subtract(BigDecimal... args) {
		Assert.notEmpty(args, "参数不能为空!");
		BigDecimal result = args[0];
		for (int i = 1; i < args.length; i++) {
			result = result.subtract(args[i]);
		}
		return result;
	}

	/**
	 * 批量相乘并标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal multiply(BigDecimal... args) {
		return multiply(true, args);
	}

	/**
	 * 批量相乘
	 * @param normalized 是否进行标准化, true为进行标准化,false为不进行标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal multiply(boolean normalized, BigDecimal... args) {
		Assert.notEmpty(args, "参数不能为空!");
		BigDecimal result = args[0];
		for (int i = 1; i < args.length; i++) {
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
	public static BigDecimal divide(BigDecimal... args) {
		return divide(true, args);
	}

	/**
	 * 批量相除,第一个数为被除数
	 * @param normalized 是否进行标准化, true为进行标准化,false为不进行标准化
	 * @author Frodez
	 * @date 2019-01-28
	 */
	public static BigDecimal divide(boolean normalized, BigDecimal... args) {
		Assert.notEmpty(args, "参数不能为空!");
		BigDecimal result = args[0];
		int precision = args.length + DefaultDecimal.PRECISION - 1;
		for (int i = 1; i < args.length; i++) {
			result = result.divide(args[i], precision, DefaultDecimal.ROUND_MODE);
		}
		if (normalized) {
			return DecimalUtil.normalize(result);
		}
		return result;
	}

}
