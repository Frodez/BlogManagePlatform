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

	private byte value;

	private String description;

	private static final Map<Byte, UserStatusEnum> enumMap;

	static {
		enumMap = new HashMap<>();
		for (UserStatusEnum iter : UserStatusEnum.values()) {
			enumMap.put(iter.value, iter);
		}
	}

	public static UserStatusEnum of(byte value) {
		return enumMap.get(value);
	}

}
