package frodez.config.error.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	USER_SERVICE_ERROR("用户信息服务失败!");

	private String description;

	public ErrorCode of(String description) {
		for (ErrorCode iter : ErrorCode.values()) {
			if (iter.description.equals(description)) {
				return iter;
			}
		}
		return null;
	}

}
