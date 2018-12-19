package info.frodez.util.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态枚举
 * @author Frodez
 * @date 2018-11-13
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {	
	
	/**
	 * 操作成功,与预期相符
	 */
	SUCCESS(ResultUtil.SUCCESS_VALUE, "成功"),
	
	/**
	 * 操作失败,与预期不符
	 */
	FAIL(ResultUtil.FAIL_VALUE, "失败"),
	
	/**
	 * 用户未登录
	 */
	NOT_LOGIN(ResultUtil.NOT_LOGIN_VALUE, "未登录"),
	
	/**
	 * 未通过验证
	 */
	NO_AUTH(ResultUtil.NO_AUTH_VALUE, "未通过验证"),
	
	/**
	 * 缺少操作权限
	 */
	NO_ACCESS(ResultUtil.NO_ACCESS_VALUE, "无权限");
	
	private int value;
	
	private String description;
	
	public ResultEnum of(int value) {
		for(ResultEnum iter : ResultEnum.values()) {
			if(iter.value == value) {
				return iter;
			}
		}
		return null;
	}

}
