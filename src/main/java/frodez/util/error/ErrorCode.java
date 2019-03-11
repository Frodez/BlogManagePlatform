package frodez.util.error;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	USER_SERVICE_ERROR("用户信息服务失败!");

	private String description;

	private static final Map<String, ErrorCode> enumMap;

	static {
		enumMap = new HashMap<>();
		for (ErrorCode iter : ErrorCode.values()) {
			enumMap.put(iter.description, iter);
		}
	}

	public static ErrorCode of(String description) {
		return enumMap.get(description);
	}

}
