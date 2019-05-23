package frodez.constant.enums.task;

import frodez.constant.annotations.decoration.EnumCheckable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 * @author Frodez
 * @date 2019-03-20
 */
@EnumCheckable
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

	private static final Map<Byte, StatusEnum> enumMap;

	static {
		vals = Arrays.asList(StatusEnum.values()).stream().map(StatusEnum::getVal).collect(Collectors
			.toUnmodifiableList());
		descs = Arrays.asList(StatusEnum.values()).stream().map(StatusEnum::getDesc).collect(Collectors
			.toUnmodifiableList());
		enumMap = new HashMap<>();
		for (StatusEnum iter : StatusEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	public static StatusEnum of(Byte value) {
		return enumMap.get(value);
	}

}
