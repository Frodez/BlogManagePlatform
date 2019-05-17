package frodez.constant.settings;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;

/**
 * 默认字符编码设置
 * @author Frodez
 * @date 2019-03-27
 */
@UtilityClass
public class DefCharset {

	/**
	 * 默认编码名称
	 */
	public static final String UTF_8 = "UTF-8";

	/**
	 * 默认Charset
	 */
	public static final Charset UTF_8_CHARSET = StandardCharsets.UTF_8;

}
