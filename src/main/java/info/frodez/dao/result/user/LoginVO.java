package info.frodez.dao.result.user;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 用户登录返回参数
 * @author Frodez
 * @date 2018-12-03
 */
@Data
public class LoginVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 登录token
	 */
	private String token;
	
	/**
	 * 权限名称
	 */
	private List<String> authorities;

}
