package info.frodez.dao.param.user;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 用户登录请求参数
 * @author Frodez
 * @date 2018-12-02
 */
@Data
public class LoginDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	@NotNull(message = "用户名不能为空!")
	private String username;

	/**
	 * 密码
	 */
	@NotNull(message = "用户名不能为空!")
	private String password;

}
