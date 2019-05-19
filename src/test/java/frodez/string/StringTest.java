//package frodez.string;
//
//import frodez.util.common.DecimalUtil;
//import frodez.util.common.StrUtil;
//import java.math.BigDecimal;
//import java.util.Random;
//import java.util.UUID;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class StringTest {
//
//	@Test
//	@SuppressWarnings("unused")
//	public void test() {
//		int testTimes = 500 * 10000;
//		int except = 5;
//		int testRounds = except + 1000;
//		long time;
//		int randomMax = 5;
//		Random random = new Random();
//		int[] lengthRecord = new int[testRounds];
//		long[] duration1 = new long[testRounds];
//		long[] duration2 = new long[testRounds];
//		long[] duration3 = new long[testRounds];
//		String[] results = new String[testRounds];
//		long start = System.currentTimeMillis();
//		for (int t = 1; t <= testRounds; t++) {
//			int arrayLength = random.nextInt(randomMax) + 2;
//			lengthRecord[t - 1] = arrayLength;
//			String[] arr = new String[arrayLength];
//			for (int i = 0; i < arrayLength; i++) {
//				int repeat = random.nextInt(randomMax) + 1;
//				int one = random.nextInt(32 * repeat);
//				int two = random.nextInt(32 * repeat);
//				arr[i] = UUID.randomUUID().toString().repeat(repeat).substring(Math.min(one, two) > 0 ? Math.min(one,
//					two) - 1 : Math.min(one, two), Math.max(one, two));
//			}
//			results[t - 1] = String.join(", ", arr);
//
//			//			for (int i = 0; i < testTimes; ++i) {
//			//				String str = "";
//			//				for (int j = 0; j < arrayLength; ++j) {
//			//					str = str + arr[j];
//			//				}
//			//			}
//
//			String firstResult = "";
//			for (int j = 0; j < arrayLength; ++j) {
//				firstResult = firstResult.concat(arr[j]);
//			}
//			String secondResult = StrUtil.concat(arr);
//			StringBuilder builder = new StringBuilder();
//			for (int j = 0; j < arr.length; ++j) {
//				builder.append(arr[j]);
//			}
//			String thirdResult = builder.toString();
//			if (!firstResult.equals(secondResult) || !firstResult.equals(thirdResult) || !secondResult.equals(
//				thirdResult)) {
//				throw new RuntimeException("firstResult:" + firstResult + ";\n" + "secondResult:" + secondResult + ";\n"
//					+ "thirdResult" + thirdResult);
//			}
//			time = System.currentTimeMillis();
//			for (int i = 0; i < testTimes; ++i) {
//				String str = "";
//				for (int j = 0; j < arrayLength; ++j) {
//					str = str.concat(arr[j]);
//				}
//			}
//			duration1[t - 1] = System.currentTimeMillis() - time;
//
//			time = System.currentTimeMillis();
//			for (int i = 0; i < testTimes; ++i) {
//				String str = StrUtil.concat(arr);
//			}
//			duration2[t - 1] = System.currentTimeMillis() - time;
//
//			time = System.currentTimeMillis();
//			for (int i = 0; i < testTimes; ++i) {
//				String str = "";
//				StringBuilder sb = new StringBuilder();
//				for (int j = 0; j < arr.length; ++j) {
//					sb.append(arr[j]);
//				}
//				str = sb.toString();
//			}
//			duration3[t - 1] = System.currentTimeMillis() - time;
//		}
//		//		System.out.println("每次测试次数:" + testTimes);
//		//		System.out.println("String.concat 拼接测试,完成时间:\n" + time(duration1, except));
//		//		System.out.println("StrUtil.concat 拼接测试,完成时间:\n" + time(duration2, except));
//		//		System.out.println("StringBuilder 拼接测试,完成时间:\n" + time(duration3, except));
//		System.out.println("以StrUtil.concat消耗时间为基准100,字符串,数组长度和消耗时间依次为\n" + show(results, lengthRecord, duration1,
//			duration2, duration3, except));
//		System.out.println("总耗时:" + (System.currentTimeMillis() - start) + "ms");
//	}
//
//	public static String arrayLength(int[] lengthRecord, int except) {
//		StringBuilder builder = new StringBuilder();
//		for (int i = except; i < lengthRecord.length; i++) {
//			builder.append(lengthRecord[i]);
//			if (i != lengthRecord.length - 1) {
//				builder.append(", ");
//			}
//		}
//		return builder.toString();
//	}
//
//	public static String show(String[] strings, int[] lengthRecord, long[] duration1, long[] duration2,
//		long[] duration3, int except) {
//		StringBuilder builder = new StringBuilder();
//		BigDecimal total1 = BigDecimal.ZERO;
//		BigDecimal total2 = BigDecimal.ZERO;
//		BigDecimal total3 = BigDecimal.ZERO;
//		for (int i = except; i < duration1.length; i++) {
//			builder.append("第" + (i - except + 1) + "次:");
//			builder.append("字符串数组长度:" + lengthRecord[i]);
//			builder.append(", ");
//			builder.append("字符串内容:" + strings[i]);
//			builder.append("\n");
//			BigDecimal percent1 = DecimalUtil.divide(BigDecimal.valueOf(duration1[i] * 100), BigDecimal.valueOf(
//				duration2[i]));
//			total1 = total1.add(percent1);
//			builder.append(percent1);
//			builder.append(", ");
//			BigDecimal percent2 = DecimalUtil.divide(BigDecimal.valueOf(duration2[i] * 100), BigDecimal.valueOf(
//				duration2[i]));
//			total2 = total2.add(percent2);
//			builder.append(percent2);
//			builder.append(", ");
//			BigDecimal percent3 = DecimalUtil.divide(BigDecimal.valueOf(duration3[i] * 100), BigDecimal.valueOf(
//				duration2[i]));
//			total3 = total3.add(percent3);
//			builder.append(percent3);
//			builder.append(";\n");
//		}
//		builder.append("平均性能:");
//		builder.append(DecimalUtil.divide(total1.multiply(new BigDecimal(100)), total2));
//		builder.append(", ");
//		builder.append(DecimalUtil.divide(total2.multiply(new BigDecimal(100)), total2));
//		builder.append(", ");
//		builder.append(DecimalUtil.divide(total3.multiply(new BigDecimal(100)), total2));
//		builder.append(";");
//		return builder.toString();
//	}
//
//	public static String time(long[] duration, int except) {
//		StringBuilder builder = new StringBuilder();
//		for (int i = except; i < duration.length; i++) {
//			builder.append(duration[i]);
//			if (i != duration.length - 1) {
//				builder.append(", ");
//			}
//		}
//		return builder.toString();
//	}
//
//}
