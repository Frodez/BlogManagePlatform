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
 * 修改操作类型枚举
 * @see OperateEnum
 * @author Frodez
 * @date 2019-03-17
 */
@EnumCheckable
@AllArgsConstructor
public enum ModifyEnum {

	/**
	 * 新增
	 */
	INSERT((byte) 1, "新增"),
	/**
	 * 删除
	 */
	DELETE((byte) 2, "删除"),
	/**
	 * 修改
	 */
	UPDATE((byte) 3, "修改");

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

	/**
	 * 介绍
	 */
	@Getter
	private static String introduction;

	private static final Map<Byte, ModifyEnum> enumMap;

	static {
		vals = Collections.unmodifiableList(Arrays.asList(ModifyEnum.values()).stream().map(ModifyEnum::getVal).collect(
			Collectors.toList()));
		descs = Collections.unmodifiableList(Arrays.asList(ModifyEnum.values()).stream().map(ModifyEnum::getDesc)
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
		for (ModifyEnum iter : ModifyEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	/**
	 * 转化
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static ModifyEnum of(Byte value) {
		return enumMap.get(value);
	}

}
