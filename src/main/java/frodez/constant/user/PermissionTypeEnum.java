package frodez.constant.user;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限类型枚举
 * @author Frodez
 * @date 2018-12-04
 */
@Getter
@AllArgsConstructor
public enum PermissionTypeEnum {

	/**
	 * 0:所有类型请求
	 */
	ALL((byte) 0, "所有类型请求"),
	/**
	 * 1:GET类型请求
	 */
	GET((byte) 1, "GET类型请求"),
	/**
	 * 2:POST类型请求
	 */
	POST((byte) 2, "POST类型请求"),
	/**
	 * 3:DELETE类型请求
	 */
	DELETE((byte) 3, "DELETE类型请求"),
	/**
	 * 4:PUT类型请求
	 */
	PUT((byte) 4, "PUT类型请求");

	private byte value;

	private String description;

	private static final Map<Byte, PermissionTypeEnum> enumMap;

	static {
		enumMap = new HashMap<>();
		for (PermissionTypeEnum iter : PermissionTypeEnum.values()) {
			enumMap.put(iter.value, iter);
		}
	}

	public static PermissionTypeEnum of(byte value) {
		return enumMap.get(value);
	}

}
