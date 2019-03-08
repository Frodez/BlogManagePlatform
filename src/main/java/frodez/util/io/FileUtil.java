package frodez.util.io;

import com.google.common.io.Files;
import frodez.constant.setting.DefCode;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class FileUtil {

	public static String fileToString(String uri) throws IOException, URISyntaxException {
		return fileToString(uri, DefCode.UTF_8);
	}

	public static String fileToString(String uri, String charset) throws IOException, URISyntaxException {
		return Files.asCharSource(new File(new URI(uri)), Charset.forName(charset)).read();
	}

	public static void stringToFile(String uri, String content) throws IOException, URISyntaxException {
		Files.asCharSink(new File(new URI(uri)), Charset.forName(DefCode.UTF_8)).write(content);
	}

	public static void stringToFile(String uri, String charset, String content) throws IOException, URISyntaxException {
		Files.asCharSink(new File(new URI(uri)), Charset.forName(charset)).write(content);
	}

}
