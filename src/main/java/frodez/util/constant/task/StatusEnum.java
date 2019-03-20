package frodez.util.constant.task;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 * @author Frodez
 * @date 2019-03-20
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

	/**
	 * 1:活跃中
	 */
	ACTIVE((byte) 1, "活跃中"),
	/**
	 * 2:不活跃
	 */
	PAUSED((byte) 2, "不活跃");

	private byte val;

	private String desc;

	private static final Map<Byte, StatusEnum> enumMap;

	static {
		enumMap = new HashMap<>();
		for (StatusEnum iter : StatusEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	public static StatusEnum of(byte value) {
		return enumMap.get(value);
	}

}
