package frodez.util.io;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import frodez.util.constant.setting.DefCode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtil {

	/**
	 * 读取文件数据到字符串
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static String readString(String uri) throws IOException, URISyntaxException {
		return readString(uri, DefCode.UTF_8_CHARSET);
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
	 * 读取文件数据到字符串List
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static List<String> readStrings(String uri) throws IOException, URISyntaxException {
		return readStrings(uri, DefCode.UTF_8_CHARSET);
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
	 * 向文件写入字符串,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeString(String content, String uri) throws IOException, URISyntaxException {
		writeString(content, uri, false);
	}

	/**
	 * 向文件写入字符串,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeString(String content, String uri, boolean isAppend) throws IOException,
		URISyntaxException {
		if (isAppend) {
			Files.asCharSink(new File(new URI(uri)), Charset.forName(DefCode.UTF_8), FileWriteMode.APPEND).write(
				content);
		} else {
			Files.asCharSink(new File(new URI(uri)), Charset.forName(DefCode.UTF_8)).write(content);
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
	public static byte[] readByte(String uri) throws IOException, URISyntaxException {
		return Files.asByteSource(new File(new URI(uri))).read();
	}

	/**
	 * 向文件写入byte数组,默认覆盖原文件
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeByte(byte[] content, String uri) throws IOException, URISyntaxException {
		writeByte(content, uri, false);
	}

	/**
	 * 向文件写入byte数组,需指定是否覆盖:true为不覆盖,false为覆盖
	 * @author Frodez
	 * @date 2019-03-09
	 */
	public static void writeByte(byte[] content, String uri, boolean isAppend) throws IOException, URISyntaxException {
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
