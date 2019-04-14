package frodez;

import frodez.util.common.StrUtil;
import frodez.util.constant.setting.DefStr;
import java.util.UUID;

public class StringTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		int testLength = 10000000;
		int arrayLength = 5;
		int testRounds = 3;
		String[] arr = new String[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			arr[i] = UUID.randomUUID().toString();
			//			for (int j = 0; j <= i; j++) {
			//				arr[i] = arr[i].concat(arr[i]);
			//			}
		}
		long start;
		long duration;
		for (int time = 1; time <= testRounds; time++) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("第" + time + "次测试开始!");

			start = System.currentTimeMillis();
			for (int i = 0; i < testLength; ++i) {
				String str = "";
				for (int j = 0; j < arrayLength; ++j) {
					str = str + arr[j];
				}
			}
			duration = System.currentTimeMillis() - start;
			System.out.println("String 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + duration);

			start = System.currentTimeMillis();
			for (int i = 0; i < testLength; ++i) {
				String str = "";
				for (int j = 0; j < arrayLength; ++j) {
					str = str.concat(arr[j]);
				}
			}
			duration = System.currentTimeMillis() - start;
			System.out.println("String.concat 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + duration);

			start = System.currentTimeMillis();
			for (int i = 0; i < testLength; ++i) {
				String str = StrUtil.concat(arr);
			}
			duration = System.currentTimeMillis() - start;
			System.out.println("StrUtil.concat 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间"
				+ duration);

			start = System.currentTimeMillis();
			for (int i = 0; i < testLength; ++i) {
				String str = "";
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < arr.length; ++j) {
					sb.append(arr[j]);
				}
				str = sb.toString();
			}
			duration = System.currentTimeMillis() - start;
			System.out.println("StringBuilder 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + duration);

			start = System.currentTimeMillis();
			for (int i = 0; i < testLength; ++i) {
				String str = String.join(DefStr.SEPERATOR, arr);
			}
			duration = System.currentTimeMillis() - start;
			//System.out.println(String.join(DefStr.SEPERATOR, arr));
			System.out.println("String.join 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + duration);

			start = System.currentTimeMillis();
			for (int i = 0; i < testLength; ++i) {
				String str = StrUtil.join("1", DefStr.SEPERATOR, arr);
			}
			duration = System.currentTimeMillis() - start;
			//System.out.println(StrUtil.join("1", DefStr.SEPERATOR, arr));
			System.out.println("StrUtil.join 拼接测试,测试长度" + testLength + ",测试字符串数组长度" + arr.length + ",完成时间" + duration);

			System.out.println("第" + time + "次测试结束!");
		}
	}

}
