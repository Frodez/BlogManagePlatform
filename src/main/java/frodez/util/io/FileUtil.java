package frodez.util.io;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import frodez.util.constant.setting.DefCharset;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * 文件工具类
 * @author Frodez
 * @date 2019-03-11
 */
@UtilityClass
public class FileUtil {

	/**
	 * 读取文件数据到字符串,默认UTF-8
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static String readString(File file) throws IOException {
		return readString(file, DefCharset.UTF_8_CHARSET);
	}

	/**
	 * 读取文件数据到字符串,需指定Charset
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static String readString(File file, Charset charset) throws IOException {
		return Files.asCharSource(file, charset).read();
	}

	/**
	 * 读取文件数据到字符串,默认UTF-8
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static String readString(String uri) throws IOException, URISyntaxException {
		return readString(uri, DefCharset.UTF_8_CHARSET);
	}

	/**
	 * 读取文件数据到字符串,需指定Charset
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static String readString(String uri, Charset charset) throws IOException, URISyntaxException {
		return Files.asCharSource(new File(new URI(uri)), charset).read();
	}

	/**
	 * 读取文件数据到字符串List,默认UTF-8
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static List<String> readStrings(File file) throws IOException, URISyntaxException {
		return readStrings(file, DefCharset.UTF_8_CHARSET);
	}

	/**
	 * 读取文件数据到字符串List,需指定Charset
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static List<String> readStrings(File file, Charset charset) throws IOException, URISyntaxException {
		return new ArrayList<>(Files.asCharSource(file, charset).readLines());
	}

	/**
	 * 读取文件数据到字符串List,默认UTF-8
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static List<String> readStrings(String uri) throws IOException, URISyntaxException {
		return readStrings(uri, DefCharset.UTF_8_CHARSET);
	}

	/**
	 * 读取文件数据到字符串List,需指定Charset
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static List<String> readStrings(String uri, Charset charset) throws IOException, URISyntaxException {
		return new ArrayList<>(Files.asCharSource(new File(new URI(uri)), charset).readLines());
	}

	/**
	 * 以UTF-8格式向文件写入字符串,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeString(String content, File file) throws IOException, URISyntaxException {
		writeString(content, file, false);
	}

	/**
	 * 以UTF-8格式向文件写入字符串,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeString(String content, File file, boolean isAppend) throws IOException, URISyntaxException {
		if (isAppend) {
			Files.asCharSink(file, DefCharset.UTF_8_CHARSET, FileWriteMode.APPEND).write(content);
		} else {
			Files.asCharSink(file, DefCharset.UTF_8_CHARSET).write(content);
		}
	}

	/**
	 * 以UTF-8格式向文件写入字符串,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeString(String content, String uri) throws IOException, URISyntaxException {
		writeString(content, uri, false);
	}

	/**
	 * 以UTF-8格式向文件写入字符串,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeString(String content, String uri, boolean isAppend) throws IOException,
		URISyntaxException {
		if (isAppend) {
			Files.asCharSink(new File(new URI(uri)), DefCharset.UTF_8_CHARSET, FileWriteMode.APPEND).write(content);
		} else {
			Files.asCharSink(new File(new URI(uri)), DefCharset.UTF_8_CHARSET).write(content);
		}
	}

	/**
	 * 以UTF-8格式向文件写入字符串,默认覆盖原文件,需指定Charset
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeString(String content, File file, Charset charset) throws IOException, URISyntaxException {
		writeString(content, file, charset, false);
	}

	/**
	 * 以UTF-8格式向文件写入字符串,需指定Charset,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeString(String content, File file, Charset charset, boolean isAppend) throws IOException,
		URISyntaxException {
		if (isAppend) {
			Files.asCharSink(file, charset, FileWriteMode.APPEND).write(content);
		} else {
			Files.asCharSink(file, charset).write(content);
		}
	}

	/**
	 * 向文件写入字符串,默认覆盖原文件,需指定Charset
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeString(String content, String uri, Charset charset) throws IOException, URISyntaxException {
		writeString(content, uri, charset, false);
	}

	/**
	 * 向文件写入字符串,需指定Charset,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeString(String content, String uri, Charset charset, boolean isAppend) throws IOException,
		URISyntaxException {
		if (isAppend) {
			Files.asCharSink(new File(new URI(uri)), charset, FileWriteMode.APPEND).write(content);
		} else {
			Files.asCharSink(new File(new URI(uri)), charset).write(content);
		}
	}

	/**
	 * 读取文件数据到byte数组
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static byte[] readBytes(File file) throws IOException {
		return Files.asByteSource(file).read();
	}

	/**
	 * 读取文件数据到byte数组
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static byte[] readBytes(String uri) throws IOException, URISyntaxException {
		return Files.asByteSource(new File(new URI(uri))).read();
	}

	/**
	 * 向文件写入byte数组,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeBytes(byte[] content, File file) throws IOException, URISyntaxException {
		writeBytes(content, file, false);
	}

	/**
	 * 向文件写入byte数组,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeBytes(byte[] content, File file, boolean isAppend) throws IOException, URISyntaxException {
		if (isAppend) {
			Files.asByteSink(file, FileWriteMode.APPEND).write(content);
		} else {
			Files.asByteSink(file).write(content);
		}
	}

	/**
	 * 向文件写入byte数组,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeBytes(byte[] content, String uri) throws IOException, URISyntaxException {
		writeBytes(content, uri, false);
	}

	/**
	 * 向文件写入byte数组,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeBytes(byte[] content, String uri, boolean isAppend) throws IOException, URISyntaxException {
		if (isAppend) {
			Files.asByteSink(new File(new URI(uri)), FileWriteMode.APPEND).write(content);
		} else {
			Files.asByteSink(new File(new URI(uri))).write(content);
		}
	}

	/**
	 * 将输入流输入数据转入文件中,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void transfer(InputStream input, File file) throws IOException, URISyntaxException {
		transfer(input, file, false);
	}

	/**
	 * 将输入流输入数据转入文件中,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void transfer(InputStream input, File file, boolean isAppend) throws IOException, URISyntaxException {
		if (isAppend) {
			Files.asByteSink(file, FileWriteMode.APPEND).writeFrom(input);
		} else {
			Files.asByteSink(file).writeFrom(input);
		}
	}

	/**
	 * 将输入流输入数据转入文件中,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void transfer(InputStream input, String uri) throws IOException, URISyntaxException {
		transfer(input, uri, false);
	}

	/**
	 * 将输入流输入数据转入文件中,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void transfer(InputStream input, String uri, boolean isAppend) throws IOException,
		URISyntaxException {
		if (isAppend) {
			Files.asByteSink(new File(new URI(uri)), FileWriteMode.APPEND).writeFrom(input);
		} else {
			Files.asByteSink(new File(new URI(uri))).writeFrom(input);
		}
	}

}
