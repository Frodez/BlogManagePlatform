package frodez.constant.common;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除状态枚举
 * @author Frodez
 * @date 2018-12-04
 */
@Getter
@AllArgsConstructor
public enum DeleteEnum {

	/**
	 * 未删除
	 */
	NO((byte) 1, "未删除"),
	/**
	 * 已删除
	 */
	YES((byte) 2, "已删除");

	private byte val;

	private String desc;

	private static final Map<Byte, DeleteEnum> enumMap;

	static {
		enumMap = new HashMap<>();
		for (DeleteEnum iter : DeleteEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	public static DeleteEnum of(byte value) {
		return enumMap.get(value);
	}

}
