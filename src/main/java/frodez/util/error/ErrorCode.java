package frodez.util.error;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常相关枚举
 * @author Frodez
 * @date 2019-04-15
 */
@AllArgsConstructor
public enum ErrorCode {

	USER_SERVICE_ERROR("用户信息服务失败!"),

	AUTHORITY_SERVICE_ERROR("权限信息服务失败!"),

	TASK_SERVICE_ERROR("定时任务服务失败!");

	@Getter
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
