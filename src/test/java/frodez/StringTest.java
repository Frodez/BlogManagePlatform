package frodez;

import java.util.UUID;

public class StringTest {

	public static void main(String[] args) {
		int testLength = 100000000;
		int arrayLength = 10;
		String[] arr = new String[arrayLength];

		long start = System.currentTimeMillis();
		String testStr = UUID.randomUUID().toString();
		System.out.println("首次生成randomUUID耗时：" + (System.currentTimeMillis() - start));

		//		start = new Date();
		//		for (int i = 0; i < testLength; i++) {
		//			testStr = UUID.randomUUID().toString();
		//		}
		//		System.out.println("非首次生成randomUUID " + testLength + "次耗时：" + (new Date().getTime() - start.getTime()));

		@SuppressWarnings("unused")
		String str = "";
		start = System.currentTimeMillis();
		for (int i = 0; i < testLength; i++) {
			str = "";
			for (int j = 0; j < arrayLength; j++) {
				str = testStr + testStr;
			}
		}
		System.out.println("String 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + (System
			.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < testLength; i++) {
			str = "";
			for (int j = 0; j < arrayLength; j++) {
				str = testStr.concat(testStr);
			}
		}
		System.out.println("String.concat 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + (System
			.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < testLength; i++) {
			str = "";
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < arr.length; j++) {
				sb.append(testStr);
			}
			str = sb.toString();
		}
		System.out.println("StringBuilder 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + (System
			.currentTimeMillis() - start));
	}

}
