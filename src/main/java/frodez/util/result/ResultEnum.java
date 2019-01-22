package frodez.util.result;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
	SUCCESS(1000, "成功"),
	/**
	 * 操作失败,与预期不符
	 */
	FAIL(1001, "失败"),
	/**
	 * 用户未登录
	 */
	NOT_LOGIN(2001, "未登录"),
	/**
	 * 未通过验证
	 */
	NO_AUTH(2002, "未通过验证"),
	/**
	 * 缺少操作权限
	 */
	NO_ACCESS(2003, "无权限"),
	/**
	 * 重复请求
	 */
	REPEAT_REQUEST(2004, "重复请求");

	private int value;

	private String description;

	public ResultEnum of(int value) {
		for (ResultEnum iter : ResultEnum.values()) {
			if (iter.value == value) {
				return iter;
			}
		}
		return null;
	}

}
