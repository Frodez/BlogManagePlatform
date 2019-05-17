package frodez.constant.enums.common;

import frodez.constant.annotations.decoration.EnumCheckable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除状态枚举
 * @author Frodez
 * @date 2018-12-04
 */
@EnumCheckable
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

	private static final Map<Byte, DeleteEnum> enumMap;

	static {
		vals = Collections.unmodifiableList(Arrays.asList(DeleteEnum.values()).stream().map(DeleteEnum::getVal).collect(
			Collectors.toList()));
		descs = Collections.unmodifiableList(Arrays.asList(DeleteEnum.values()).stream().map(DeleteEnum::getDesc)
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
		for (DeleteEnum iter : DeleteEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	/**
	 * 转化
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static DeleteEnum of(byte value) {
		return enumMap.get(value);
	}

}
