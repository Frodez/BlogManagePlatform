package info.frodez.constant.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 * @author Frodez
 * @date 2018-11-14
 */
@Getter
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

	private byte value;

	private String description;

	public UserStatusEnum of(byte value) {
		for(UserStatusEnum iter : UserStatusEnum.values()) {
			if(iter.value == value) {
				return iter;
			}
		}
		return null;
	}

}
