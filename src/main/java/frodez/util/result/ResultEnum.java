package frodez.util.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 返回状态枚举
 * @author Frodez
 * @date 2018-11-13
 */
@Getter
@AllArgsConstructor
public enum ResultEnum implements Serializable {

	/**
	 * 操作成功,与预期相符
	 */
	SUCCESS(1000, HttpStatus.OK, "成功"),
	/**
	 * 操作失败,与预期不符
	 */
	FAIL(1001, HttpStatus.OK, "失败"),
	/**
	 * 请求参数错误
	 */
	ERROR_REQUEST(1002, HttpStatus.BAD_REQUEST, "请求参数错误"),
	/**
	 * 服务器错误
	 */
	ERROR_SERVICE(1003, HttpStatus.INTERNAL_SERVER_ERROR, "服务器错误"),
	/**
	 * 未登录
	 */
	NOT_LOGIN(2001, HttpStatus.UNAUTHORIZED, "未登录"),
	/**
	 * 已过期
	 */
	EXPIRED(2002, HttpStatus.UNAUTHORIZED, "已过期"),
	/**
	 * 未通过验证
	 */
	NO_AUTH(2003, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "未通过验证"),

	/**
	 * 缺少操作权限
	 */
	NO_ACCESS(2004, HttpStatus.FORBIDDEN, "无权限"),
	/**
	 * 重复请求
	 */
	REPEAT_REQUEST(2005, HttpStatus.LOCKED, "重复请求");

	/**
	 * 自定义状态码
	 */
	private int val;

	/**
	 * http状态码
	 */
	private HttpStatus status;

	/**
	 * 描述
	 */
	private String desc;

	private static final Map<Integer, ResultEnum> enumMap;

	static {
		enumMap = new HashMap<>();
		for (ResultEnum iter : ResultEnum.values()) {
			enumMap.put(iter.val, iter);
		}
	}

	public static ResultEnum of(int value) {
		return enumMap.get(value);
	}

}
