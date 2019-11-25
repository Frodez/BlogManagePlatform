package frodez.string;

import frodez.util.common.DecimalUtil;
import frodez.util.common.StrUtil;
import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

public class StringTest {

	public static void main(String[] args) throws Throwable {
		for (int i = 1; i < 5; i++) {
			test(i);
		}
		for (int i = 1; i < 5; i++) {
			test(i);
		}
		for (int i = 1; i < 5; i++) {
			test(i);
		}
	}

	@SuppressWarnings("unused")
	public static void test(int randomMax) throws Throwable {
		int testTimes = 100 * 100;
		int except = 5;
		int testRounds = except + 10000;
		long time;
		Random random = new Random();
		long[] duration1 = new long[testRounds];
		long[] duration2 = new long[testRounds];
		long[] duration3 = new long[testRounds];
		long[] duration4 = new long[testRounds];
		long start = System.nanoTime();
		for (int t = 1; t <= testRounds; t++) {
			String[] arr = produce(random, randomMax);
			testify(arr);

			time = System.nanoTime();
			for (int i = 0; i < testTimes; ++i) {
				String str = "";
				for (int j = 0; j < arr.length; ++j) {
					str = str.concat(arr[j]);
				}
			}
			duration1[t - 1] = System.nanoTime() - time;

			time = System.nanoTime();
			for (int i = 0; i < testTimes; ++i) {
				String str = StrUtil.concat(arr);
			}
			duration2[t - 1] = System.nanoTime() - time;

			time = System.nanoTime();
			for (int i = 0; i < testTimes; ++i) {
				String str = StrUtil.concat(arr);
			}
			duration3[t - 1] = System.nanoTime() - time;

			time = System.nanoTime();
			for (int i = 0; i < testTimes; ++i) {
				String str = "";
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < arr.length; ++j) {
					sb.append(arr[j]);
				}
				str = sb.toString();
			}
			duration4[t - 1] = System.nanoTime() - time;
		}
		//		System.out.println("每次测试次数:" + testTimes);
		//		System.out.println("String.concat 拼接测试,完成时间:\n" + time(duration1, except));
		//		System.out.println("StrUtil.concat 拼接测试,完成时间:\n" + time(duration2, except));
		//		System.out.println("StringBuilder 拼接测试,完成时间:\n" + time(duration3, except));
		System.out.println("以StrUtil.concat消耗时间为基准100,String.concat,StrUtil.concat,StrUtil.concatV2,String.append字符串,数组长度和消耗时间依次为\n" + show(duration1,
			duration2, duration3, duration4, except));
		System.out.println("总耗时:" + (System.nanoTime() - start) / 1000000 + "ms");
	}

	private static String[] produce(Random random, int randomMax) {
		int arrayLength = random.nextInt(randomMax) + 2;
		String[] arr = new String[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			int repeat = random.nextInt(randomMax) + 1;
			int one = random.nextInt(32 * repeat);
			int two = random.nextInt(32 * repeat);
			arr[i] = UUID.randomUUID().toString().repeat(repeat).substring(Math.min(one, two) > 0 ? Math.min(one, two) - 1 : Math.min(one, two), Math
				.max(one, two));
		}
		return arr;
	}

	private static String show(long[] duration1, long[] duration2, long[] duration3, long[] duration4, int except) {
		StringBuilder builder = new StringBuilder();
		BigDecimal total1 = BigDecimal.ZERO;
		BigDecimal total2 = BigDecimal.ZERO;
		BigDecimal total3 = BigDecimal.ZERO;
		BigDecimal total4 = BigDecimal.ZERO;
		for (int i = except; i < duration1.length; i++) {
			BigDecimal percent1 = DecimalUtil.divide(BigDecimal.valueOf(duration1[i] * 100), BigDecimal.valueOf(duration2[i]));
			total1 = total1.add(percent1);
			BigDecimal percent2 = DecimalUtil.divide(BigDecimal.valueOf(duration2[i] * 100), BigDecimal.valueOf(duration2[i]));
			total2 = total2.add(percent2);
			BigDecimal percent3 = DecimalUtil.divide(BigDecimal.valueOf(duration3[i] * 100), BigDecimal.valueOf(duration2[i]));
			total3 = total3.add(percent3);
			BigDecimal percent4 = DecimalUtil.divide(BigDecimal.valueOf(duration4[i] * 100), BigDecimal.valueOf(duration2[i]));
			total4 = total4.add(percent4);
			if (duration1.length <= 10) {
				builder.append("第" + (i - except + 1) + "次:");
				builder.append("\n");
				builder.append(percent1);
				builder.append(", ");
				builder.append(percent2);
				builder.append(", ");
				builder.append(percent3);
				builder.append(", ");
				builder.append(percent4);
				builder.append(";\n");
			}
		}
		builder.append("平均耗时:");
		builder.append(DecimalUtil.divide(total1.multiply(new BigDecimal(100)), total2));
		builder.append(", ");
		builder.append(DecimalUtil.divide(total2.multiply(new BigDecimal(100)), total2));
		builder.append(", ");
		builder.append(DecimalUtil.divide(total3.multiply(new BigDecimal(100)), total2));
		builder.append(", ");
		builder.append(DecimalUtil.divide(total4.multiply(new BigDecimal(100)), total2));
		builder.append(";");
		return builder.toString();
	}

	private static void testify(String[] arr) throws Throwable {
		String firstResult = Test1(arr);
		String secondResult = Test2(arr);
		String thirdResult = Test3(arr);
		String fourthResult = Test4(arr);
		//			System.out.println("正确性测试:");
		//			System.out.println("firstResult:" + firstResult + ";\n" + "secondResult:" + secondResult + ";\n" + "thirdResult:" + thirdResult + ";\n"
		//				+ "fourthResult" + fourthResult);
		if (!firstResult.equals(secondResult) || !firstResult.equals(thirdResult) || !secondResult.equals(thirdResult) || !secondResult.equals(
			fourthResult)) {
			throw new RuntimeException();
		}
	}

	private static String Test1(String[] arr) {
		String str = "";
		for (int j = 0; j < arr.length; ++j) {
			str = str.concat(arr[j]);
		}
		return str;
	}

	private static String Test2(String[] arr) {
		return StrUtil.concat(arr);
	}

	private static String Test3(String[] arr) throws Throwable {
		return StrUtil.concat(arr);
	}

	private static String Test4(String[] arr) {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < arr.length; ++j) {
			sb.append(arr[j]);
		}
		return sb.toString();
	}

}
