package frodez;

import frodez.util.common.StrUtil;
import java.util.UUID;

public class StringTest {

	public static void main(String[] args) {
		int testLength = 10000000;
		int arrayLength = 3;
		String[] arr = new String[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			arr[i] = UUID.randomUUID().toString();
		}
		String str = "";
		long start;
		for (int time = 1; time <= 3; time++) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("第" + time + "次测试开始!");

			start = System.currentTimeMillis();
			for (int i = 0; i < testLength; ++i) {
				str = "";
				for (int j = 0; j < arrayLength; ++j) {
					str = str + arr[j];
				}
			}
			System.out.println("String 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + (System
				.currentTimeMillis() - start));

			start = System.currentTimeMillis();
			for (int i = 0; i < testLength; ++i) {
				str = "";
				for (int j = 0; j < arrayLength; ++j) {
					str = str.concat(arr[j]);
				}
			}
			System.out.println("String.concat 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + (System
				.currentTimeMillis() - start));

			start = System.currentTimeMillis();
			for (int i = 0; i < testLength; ++i) {
				str = StrUtil.concat(arr);
			}
			System.out.println("StrUtil.concat 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + (System
				.currentTimeMillis() - start));

			start = System.currentTimeMillis();
			for (int i = 0; i < testLength; ++i) {
				str = "";
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < arr.length; ++j) {
					sb.append(arr[j]);
				}
				str = sb.toString();
			}
			System.out.println("StringBuilder 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + (System
				.currentTimeMillis() - start));

			System.out.println("第" + time + "次测试结束!");
		}
	}

}
