package frodez.constant.user;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 * @author Frodez
 * @date 2018-11-14
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

	/**
	 * 0:禁用
	 */
	FORBIDDEN((byte) 0, "禁用"),
	/**
	 * 1:正常
	 */
	NORMAL((byte) 1, "正常");

	private byte val;

	private String desc;

	private static final Map<Byte, UserStatusEnum> enumMap;

	static {
		enumMap = new HashMap<>();
		for (UserStatusEnum iter : UserStatusEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	public static UserStatusEnum of(byte value) {
		return enumMap.get(value);
	}

}
