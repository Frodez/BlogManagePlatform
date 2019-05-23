package frodez.constant.enums.user;

import frodez.constant.annotations.decoration.EnumCheckable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 * @author Frodez
 * @date 2018-11-14
 */
@EnumCheckable
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

	/**
	 * 值
	 */
	@Getter
	private Byte val;

	/**
	 * 描述
	 */
	@Getter
	private String desc;

	/**
	 * 所有值
	 */
	@Getter
	private static List<Byte> vals;

	/**
	 * 所有描述
	 */
	@Getter
	private static List<String> descs;

	private static final Map<Byte, UserStatusEnum> enumMap;

	static {
		vals = Arrays.asList(UserStatusEnum.values()).stream().map(UserStatusEnum::getVal).collect(Collectors
			.toUnmodifiableList());
		descs = Arrays.asList(UserStatusEnum.values()).stream().map(UserStatusEnum::getDesc).collect(Collectors
			.toUnmodifiableList());
		enumMap = new HashMap<>();
		for (UserStatusEnum iter : UserStatusEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	public static UserStatusEnum of(Byte value) {
		return enumMap.get(value);
	}

}
