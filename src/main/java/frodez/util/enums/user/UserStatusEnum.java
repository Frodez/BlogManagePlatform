package frodez.util.enums.user;

import frodez.util.annotations.decoration.EnumCheckable;
import java.util.Arrays;
import java.util.Collections;
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
	private byte val;

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

	/**
	 * 介绍
	 */
	@Getter
	private static String introduction;

	private static final Map<Byte, UserStatusEnum> enumMap;

	static {
		vals = Collections.unmodifiableList(Arrays.asList(UserStatusEnum.values()).stream().map(UserStatusEnum::getVal)
			.collect(Collectors.toList()));
		descs = Collections.unmodifiableList(Arrays.asList(UserStatusEnum.values()).stream().map(
			UserStatusEnum::getDesc).collect(Collectors.toList()));
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < vals.size(); i++) {
			builder.append(vals.get(i).toString());
			if (i != vals.size() - 1) {
				builder.append(",");
			}
		}
		introduction = builder.toString();
		enumMap = new HashMap<>();
		for (UserStatusEnum iter : UserStatusEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	public static UserStatusEnum of(byte value) {
		return enumMap.get(value);
	}

}
