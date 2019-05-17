package frodez.util.enums.task;

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

	private static final Map<Byte, StatusEnum> enumMap;

	static {
		vals = Collections.unmodifiableList(Arrays.asList(StatusEnum.values()).stream().map(StatusEnum::getVal).collect(
			Collectors.toList()));
		descs = Collections.unmodifiableList(Arrays.asList(StatusEnum.values()).stream().map(StatusEnum::getDesc)
			.collect(Collectors.toList()));
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < vals.size(); i++) {
			builder.append(vals.get(i).toString());
			if (i != vals.size() - 1) {
				builder.append(",");
			}
		}
		introduction = builder.toString();
		enumMap = new HashMap<>();
		for (StatusEnum iter : StatusEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	public static StatusEnum of(byte value) {
		return enumMap.get(value);
	}

}
