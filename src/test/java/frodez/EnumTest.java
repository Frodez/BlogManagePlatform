package frodez;

import frodez.constant.user.PermissionTypeEnum;
import java.util.Random;

public class EnumTest {

	private static final int TIMES = 1000000000;

	public static void main(String[] args) {
		byte[] randoms = new byte[TIMES];
		Random random = new Random();
		for (int i = 0; i < TIMES; i++) {
			randoms[i] = (byte) random.nextInt(5);
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < TIMES; i++) {
			PermissionTypeEnum.of(randoms[i]);
		}
		System.out.println(System.currentTimeMillis() - start);
	}

}
