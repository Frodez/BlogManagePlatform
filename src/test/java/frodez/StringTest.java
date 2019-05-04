package frodez;

import frodez.util.common.DecimalUtil;
import frodez.util.common.StrUtil;
import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

public class StringTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		int testTimes = 1500 * 10000;
		int except = 2;
		int testRounds = except + 25;
		long time;
		int randomMax = 10;
		Random random = new Random();
		int[] lengthRecord = new int[testRounds];
		long[] duration1 = new long[testRounds];
		long[] duration2 = new long[testRounds];
		long[] duration3 = new long[testRounds];
		for (int t = 1; t <= testRounds; t++) {
			int arrayLength = random.nextInt(randomMax) + 1;
			lengthRecord[t - 1] = arrayLength;
			String[] arr = new String[arrayLength];
			for (int i = 0; i < arrayLength; i++) {
				arr[i] = UUID.randomUUID().toString();
			}

			//			for (int i = 0; i < testTimes; ++i) {
			//				String str = "";
			//				for (int j = 0; j < arrayLength; ++j) {
			//					str = str + arr[j];
			//				}
			//			}

			time = System.currentTimeMillis();
			for (int i = 0; i < testTimes; ++i) {
				String str = "";
				for (int j = 0; j < arrayLength; ++j) {
					str = str.concat(arr[j]);
				}
			}
			duration1[t - 1] = System.currentTimeMillis() - time;

			time = System.currentTimeMillis();
			for (int i = 0; i < testTimes; ++i) {
				String str = StrUtil.concat(arr);
			}
			duration2[t - 1] = System.currentTimeMillis() - time;

			time = System.currentTimeMillis();
			for (int i = 0; i < testTimes; ++i) {
				String str = "";
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < arr.length; ++j) {
					sb.append(arr[j]);
				}
				str = sb.toString();
			}
			duration3[t - 1] = System.currentTimeMillis() - time;
		}
		System.out.println("每次测试次数:" + testTimes);
		System.out.println("String.concat 拼接测试,完成时间:\n" + time(duration1, except));
		System.out.println("StrUtil.concat 拼接测试,完成时间:\n" + time(duration2, except));
		System.out.println("StringBuilder 拼接测试,完成时间:\n" + time(duration3, except));
		System.out.println("以StrUtil.concat消耗时间为基准100,数组长度和性能依次为\n" + percent(lengthRecord, duration1, duration2,
			duration3, except));
	}

	public static String arrayLength(int[] lengthRecord, int except) {
		StringBuilder builder = new StringBuilder();
		for (int i = except; i < lengthRecord.length; i++) {
			builder.append(lengthRecord[i]);
			if (i != lengthRecord.length - 1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

	public static String percent(int[] lengthRecord, long[] duration1, long[] duration2, long[] duration3, int except) {
		StringBuilder builder = new StringBuilder();
		for (int i = except; i < duration1.length; i++) {
			builder.append(lengthRecord[i]);
			builder.append(": ");
			builder.append(DecimalUtil.divide(BigDecimal.valueOf(duration1[i] * 100), BigDecimal.valueOf(duration2[i]))
				.toString());
			builder.append(", ");
			builder.append(DecimalUtil.divide(BigDecimal.valueOf(duration2[i] * 100), BigDecimal.valueOf(duration2[i]))
				.toString());
			builder.append(", ");
			builder.append(DecimalUtil.divide(BigDecimal.valueOf(duration3[i] * 100), BigDecimal.valueOf(duration2[i]))
				.toString());
			if (i != duration1.length - 1) {
				builder.append(";\n");
			}
		}
		return builder.toString();
	}

	public static String time(long[] duration, int except) {
		StringBuilder builder = new StringBuilder();
		for (int i = except; i < duration.length; i++) {
			builder.append(duration[i]);
			if (i != duration.length - 1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

}
