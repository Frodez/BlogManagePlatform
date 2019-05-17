package frodez.constant.settings;

import java.math.RoundingMode;
import lombok.experimental.UtilityClass;

/**
 * 默认Decimal设置
 * @author Frodez
 * @date 2019-03-27
 */
@UtilityClass
public class DefDecimal {

	/**
	 * 默认Decimal精度
	 */
	public static final int PRECISION = 2;

	/**
	 * 默认Decimal舍入模式
	 */
	public static final RoundingMode ROUND_MODE = RoundingMode.UP;

}
