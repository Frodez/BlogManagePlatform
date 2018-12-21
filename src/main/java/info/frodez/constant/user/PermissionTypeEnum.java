package info.frodez.constant.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
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

	public PermissionTypeEnum of(byte value) {
		for(PermissionTypeEnum iter : PermissionTypeEnum.values()) {
			if(iter.value == value) {
				return iter;
			}
		}
		return null;
	}

}
