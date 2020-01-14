package frodez.util.io;

import com.google.common.collect.ImmutableList;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import frodez.constant.settings.DefCharset;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.util.ClassUtils;

/**
 * 文件工具类<br>
 * 本工具类主要负责文件到字符,字节的读取和转换<br>
 * 由于路径处理方面的原因,建议在spring体系下读取File使用ResourceUtils,然后使用本工具类转换。<br>
 * @see org.springframework.util.ResourceUtils
 * @author Frodez
 * @date 2019-03-11
 */
@UtilityClass
public class FileUtil {

	/**
	 * 项目根路径,前面未加协议名。如果要转换为URI,请在前面加上协议名(如file)。
	 */
	public static final String PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath();

	/**
	 * 读取文件数据到字符串,默认UTF-8
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static String readString(File file) {
		return readString(file, DefCharset.UTF_8_CHARSET);
	}

	/**
	 * 读取文件数据到字符串,需指定Charset
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static String readString(File file, Charset charset) {
		return Files.asCharSource(file, charset).read();
	}

	/**
	 * 读取文件数据到字符串List,默认UTF-8<br>
	 * <strong>该字符串不可变,如需可变,需要将其转化为可变List。</strong>
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static ImmutableList<String> readStrings(File file) {
		return readStrings(file, DefCharset.UTF_8_CHARSET);
	}

	/**
	 * 读取文件数据到字符串List,需指定Charset<br>
	 * <strong>该字符串不可变,如需可变,需要将其转化为可变List。</strong>
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static ImmutableList<String> readStrings(File file, Charset charset) {
		return Files.asCharSource(file, charset).readLines();
	}

	/**
	 * 以UTF-8格式向文件写入字符串,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static void writeString(String content, File file) {
		writeString(content, file, false);
	}

	/**
	 * 以UTF-8格式向文件写入字符串,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static void writeString(String content, File file, boolean isAppend) {
		if (isAppend) {
			Files.asCharSink(file, DefCharset.UTF_8_CHARSET, FileWriteMode.APPEND).write(content);
		} else {
			Files.asCharSink(file, DefCharset.UTF_8_CHARSET).write(content);
		}
	}

	/**
	 * 以UTF-8格式向文件写入字符串,默认覆盖原文件,需指定Charset
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static void writeString(String content, File file, Charset charset) {
		writeString(content, file, charset, false);
	}

	/**
	 * 以UTF-8格式向文件写入字符串,需指定Charset,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static void writeString(String content, File file, Charset charset, boolean isAppend) {
		if (isAppend) {
			Files.asCharSink(file, charset, FileWriteMode.APPEND).write(content);
		} else {
			Files.asCharSink(file, charset).write(content);
		}
	}

	/**
	 * 读取文件数据到byte数组
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static byte[] readBytes(File file) {
		return Files.asByteSource(file).read();
	}

	/**
	 * 向文件写入byte数组,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static void writeBytes(byte[] content, File file) {
		writeBytes(content, file, false);
	}

	/**
	 * 向文件写入byte数组,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static void writeBytes(byte[] content, File file, boolean isAppend) {
		if (isAppend) {
			Files.asByteSink(file, FileWriteMode.APPEND).write(content);
		} else {
			Files.asByteSink(file).write(content);
		}
	}

	/**
	 * 将输入流输入数据转入文件中,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static void transfer(InputStream input, File file) {
		transfer(input, file, false);
	}

	/**
	 * 将输入流输入数据转入文件中,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	@SneakyThrows
	public static void transfer(InputStream input, File file, boolean isAppend) {
		if (isAppend) {
			Files.asByteSink(file, FileWriteMode.APPEND).writeFrom(input);
		} else {
			Files.asByteSink(file).writeFrom(input);
		}
	}

}
