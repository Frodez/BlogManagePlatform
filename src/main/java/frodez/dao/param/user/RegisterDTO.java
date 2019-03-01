package frodez.dao.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 用户注册请求参数
 * @author Frodez
 * @date 2019-02-02
 */
@Data
@NoArgsConstructor
@ApiModel(value = "用户注册请求参数", description = "用户注册请求参数")
public class RegisterDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	@NotNull(message = "用户名不能为空!")
	@Length(message = "用户名长度不能小于3位且不能大于25位!", min = 3, max = 25)
	@ApiModelProperty(value = "用户名", required = true)
	private String name;

	/**
	 * 密码
	 */
	@NotNull(message = "密码不能为空!")
	@Length(message = "密码长度不能小于8位且不能大于30位!", min = 8, max = 30)
	@ApiModelProperty(value = "密码", required = true)
	private String password;

	/**
	 * 昵称
	 */
	@Length(message = "昵称长度不能小于3位且不能大于25位!", min = 3, max = 25)
	@ApiModelProperty(value = "昵称", required = false)
	private String nickname;

	/**
	 * 邮箱地址
	 */
	@Email
	@ApiModelProperty(value = "邮箱地址", required = false)
	private String email;

	/**
	 * 电话号码
	 */
	@Pattern(regexp = "^[1](([3|5|8][\\\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\\\d]{8}$")
	@ApiModelProperty(value = "电话号码", required = false)
	private String phone;

}
