package frodez.util.common;

import frodez.constant.settings.DefStr;
import java.lang.reflect.Method;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * 字符串处理工具类
 * @author Frodez
 * @date 2019-03-27
 */
@UtilityClass
public class StrUtil {

	private static byte code = 0;

	private static final CharsetEncoder ASCII_ENCODER = StandardCharsets.US_ASCII.newEncoder();

	static {
		try {
			Method method = String.class.getDeclaredMethod("coder", new Class<?>[0]);
			method.setAccessible(true);
			code = (byte) method.invoke(new String());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	}

	/**
	 * 将空字符串转换为null
	 * @author Frodez
	 * @date 2019-12-07
	 */
	public static String orNull(String string) {
		if (string == null) {
			return null;
		}
		return string.isEmpty() ? null : string;
	}

	/**
	 * 将null转换为空字符串
	 * @author Frodez
	 * @date 2019-12-07
	 */
	public static String orEmpty(String string) {
		return string == null ? DefStr.EMPTY : string;
	}

	/**
	 * 批量拼接字符串。<br>
	 * 注意:<strong>当输入的字符串数组中某处为null时，会抛出异常.</strong><br>
	 * 经测试,在绝大多数场景下相对jdk的实现更快(平均20-30%左右),在最坏情况下也与其相当。<br>
	 * 与StringBuilder的非优化使用方式相比,性能提高从20%-70%不等。当拼接的字符串长度较长,或者字符串数组长度较长时,性能优势更大。
	 * @see java.lang.String#concat(String)
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String concat(String... strings) {
		if (EmptyUtil.yes(strings)) {
			throw new IllegalArgumentException("it isn't suitable for empty string.");
		}
		int size = 0;
		for (int i = 0; i < strings.length; i++) {
			//如果字符串数组某处为null,会自动抛出异常
			size = size + strings[i].length();
		}
		size = size << code;
		StringBuilder builder = new StringBuilder(size);
		for (int i = 0; i < strings.length; i++) {
			String string = strings[i];
			if (string.isEmpty()) {
				continue;
			}
			builder.append(string);
		}
		return builder.toString();
	}

	/**
	 * 批量拼接字符串。<br>
	 * 注意:<strong>当输入的字符串数组中某处为null时，会抛出异常.</strong><br>
	 * 经测试,在绝大多数场景下相对jdk的实现更快(平均20-30%左右),在最坏情况下也与其相当。<br>
	 * 与StringBuilder的非优化使用方式相比,性能提高从20%-70%不等。当拼接的字符串长度较长,或者字符串数组长度较长时,性能优势更大。
	 * @see java.lang.String#concat(String)
	 * @author Frodez
	 * @date 2019-04-01
	 */
	public static String concat(Collection<String> strings) {
		if (EmptyUtil.yes(strings)) {
			throw new IllegalArgumentException("it isn't suitable for empty string.");
		}
		int size = 0;
		for (String string : strings) {
			//如果字符串数组某处为null,会自动抛出异常
			size = size + string.length();
		}
		size = size << code;
		StringBuilder builder = new StringBuilder(size);
		for (String string : strings) {
			if (string.isEmpty()) {
				continue;
			}
			builder.append(string);
		}
		return builder.toString();
	}

	/**
	 * 优雅join,专门处理null和空字符串<br>
	 * <strong>如果不需要处理null和空字符串,请不要用本方法!!!</strong>
	 * @author Frodez
	 * @date 2019-12-14
	 */
	public static String join(CharSequence delimiter, CharSequence... elements) {
		CharSequence[] strings = EmptyUtil.trim(elements);
		return strings.length == 0 ? DefStr.EMPTY : String.join(delimiter, strings);
	}

	/**
	 * 首字母大写
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static String upperFirst(String string) {
		if (EmptyUtil.yes(string)) {
			throw new IllegalArgumentException("it isn't suitable for empty string.");
		}
		StringBuilder builder = new StringBuilder(string.length());
		upper(builder, string, 0);
		return builder.toString();
	}

	/**
	 * 首字母小写
	 * @author Frodez
	 * @date 2019-04-15
	 */
	public static String lowerFirst(String string) {
		if (EmptyUtil.yes(string)) {
			throw new IllegalArgumentException("it isn't suitable for empty string.");
		}
		StringBuilder builder = new StringBuilder(string.length());
		lower(builder, string, 0);
		return builder.toString();
	}

	private void upper(StringBuilder builder, String string, int start) {
		int end = string.length();
		if (start == end) {
			return;
		}
		builder.append(Character.toUpperCase(string.charAt(0)));
		if (start + 1 < end) {
			builder.append(string.substring(start + 1));
		}
	}

	private void upper(StringBuilder builder, String string, int start, int end) {
		if (start == end) {
			return;
		}
		builder.append(Character.toUpperCase(string.charAt(0)));
		if (start + 1 < end) {
			builder.append(string.substring(start + 1, end));
		}
	}

	private void lower(StringBuilder builder, String string, int start) {
		int end = string.length();
		if (start == end) {
			return;
		}
		builder.append(Character.toLowerCase(string.charAt(0)));
		if (start + 1 < end) {
			builder.append(string.substring(start + 1));
		}
	}

	private void lower(StringBuilder builder, String string, int start, int end) {
		if (start == end) {
			return;
		}
		builder.append(Character.toLowerCase(string.charAt(0)));
		if (start + 1 < end) {
			builder.append(string.substring(start + 1, end));
		}
	}

	/**
	 * 转换为驼峰命名法<br>
	 * 默认以"-"为分隔符,将首字母变为小写,将之后的每个分词的首字母变为大写。如果分隔符无效,则会将首字母变为小写。<br>
	 * @author Frodez
	 * @date 2019-04-17
	 */
	public static String toCamelCase(String string) {
		return toCamelCase("-", string);
	}

	/**
	 * 转换为驼峰命名法<br>
	 * 以设定的分隔符作为标准,将首字母变为小写,将之后的每个分词的首字母变为大写。如果分隔符无效,则会将首字母变为小写。<br>
	 * @author Frodez
	 * @date 2019-04-17
	 */
	public static String toCamelCase(String delimiter, String string) {
		if (string == null || delimiter == null) {
			throw new IllegalArgumentException("when string is null, delimiter can't be null either.");
		}
		StringBuilder builder = new StringBuilder(string.length() << code);
		int from = builder.indexOf(delimiter);
		if (from < 0) {
			//未找到分隔符,直接将原字符串首字母小写。
			lower(builder, string, 0);
			return builder.toString();
		}
		lower(builder, string, 0, from);
		int skip = delimiter.length();
		//上个分隔符的结束处
		from = from + skip;
		while (true) {
			//下个分隔符的起始处
			int next = string.indexOf(delimiter, from);
			if (next < 0) {
				//如果未找到下个分隔符
				upper(builder, string, from);
				break;
			} else {
				//如果找到了
				upper(builder, string, from, next);
				from = next + skip;
			}
		}
		return builder.toString();
	}

	/**
	 * 不使用正则表达式的spilt
	 * @author Frodez
	 * @date 2019-12-24
	 */
	public static String[] split(String delimiter, String string) {
		int from = string.indexOf(delimiter);
		if (from < 0) {
			//未找到
			return new String[] { string };
		}
		//如果找到,则分割
		return split(from, delimiter, string).finish();
	}

	/**
	 * 不使用正则表达式的spilt
	 * @param from 第一个分隔符的起始点
	 * @author Frodez
	 * @date 2019-12-24
	 */
	private static StringArray split(int from, String delimiter, String string) {
		int skip = delimiter.length();
		//可能的长度
		StringArray builder = new StringArray(string.length() / (skip << 2));
		//加入第一段
		builder.append(string.substring(0, from));
		//上个分隔符的结束处
		from = from + skip;
		while (true) {
			//下个分隔符的起始处
			int next = string.indexOf(delimiter, from);
			if (next < 0) {
				//如果未找到下个分隔符
				if (from != string.length()) {
					builder.append(string.substring(from));
				}
				break;
			} else {
				//说明两个分隔符之间有字符
				if (next != from) {
					builder.append(string.substring(from, next));
				}
				from = next + skip;
			}
		}
		return builder;
	}

	/**
	 * 可变长字符数组
	 * @author Frodez
	 * @date 2019-12-24
	 */
	private class StringArray {

		//设置一个稍小的值,毕竟用不到那么多
		private static final int MAX_ARRAY_SIZE = 65536;

		/**
		 * 数据
		 */
		private String[] elements;

		/**
		 * 总容量
		 */
		private int length;

		/**
		 * 当前头部位置(elements[head]为null)
		 */
		private int head;

		/**
		 * 按照设置初始化
		 * @param initialLength 基础容量
		 */
		public StringArray(int initialLength) {
			length = initialLength;
			elements = new String[length];
			head = 0;
		}

		/**
		 * 添加数据
		 * @author Frodez
		 * @date 2019-12-24
		 */
		public void append(String data) {
			if (data == null) {
				throw new NullPointerException();
			}
			if (head == length) {
				elements = grow();
			}
			elements[head++] = data;
		}

		private String[] grow() {
			if (length > MAX_ARRAY_SIZE) {
				throw new RuntimeException("size must be no more than" + MAX_ARRAY_SIZE);
			}
			length = length << 1;
			return Arrays.copyOf(elements, length);
		}

		/**
		 * 转换为数组
		 * @author Frodez
		 * @date 2019-12-24
		 */
		public String[] finish() {
			return length == head ? elements : Arrays.copyOf(elements, head);
		}

	}

	/**
	 * 不使用正则表达式的spilt
	 * @author Frodez
	 * @date 2019-12-24
	 */
	public static List<String> splitList(String string, String regex, int limit) {
		char ch = 0;
		int off = 0;
		int next = 0;
		boolean limited = limit > 0;
		ArrayList<String> list = new ArrayList<>();
		while ((next = string.indexOf(ch, off)) != -1) {
			if (!limited || list.size() < limit - 1) {
				list.add(string.substring(off, next));
				off = next + 1;
			} else { // last one
				//assert (list.size() == limit - 1);
				int last = string.length();
				list.add(string.substring(off, last));
				off = last;
				break;
			}
		}
		// If no match was found, return this
		if (off == 0) {
			return List.of(string);
		}

		// Add remaining segment
		if (!limited || list.size() < limit) {
			list.add(string.substring(off, string.length()));
		}

		// Construct result
		int resultSize = list.size();
		if (limit == 0) {
			while (resultSize > 0 && list.get(resultSize - 1).isEmpty()) {
				resultSize--;
			}
		}
		return list.subList(0, resultSize);
	}

	/**
	 * 是否为ascii字符串
	 * @author Frodez
	 * @date 2019-12-07
	 */
	public boolean isAscii(String string) {
		return ASCII_ENCODER.canEncode(string);
	}

}
