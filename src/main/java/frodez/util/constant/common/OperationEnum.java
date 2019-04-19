package frodez.util.constant.common;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举
 * @see ModifyEnum
 * @author Frodez
 * @date 2019-03-17
 */
@Getter
@AllArgsConstructor
public enum OperationEnum {

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
	UPDATE((byte) 3, "修改"),
	/**
	 * 查询
	 */
	SELECT((byte) 4, "查询");

	private byte val;

	private String desc;

	private static final Map<Byte, OperationEnum> enumMap;

	static {
		enumMap = new HashMap<>();
		for (OperationEnum iter : OperationEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	public static OperationEnum of(byte value) {
		return enumMap.get(value);
	}

}
