package frodez.util.constant.common;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 修改操作类型枚举
 * @see OperateEnum
 * @author Frodez
 * @date 2019-03-17
 */
@Getter
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

	private byte val;

	private String desc;

	private static final Map<Byte, ModifyEnum> enumMap;

	static {
		enumMap = new HashMap<>();
		for (ModifyEnum iter : ModifyEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	public static ModifyEnum of(byte value) {
		return enumMap.get(value);
	}

}
