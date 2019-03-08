package frodez.dao.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 用户登录请求参数
 * @author Frodez
 * @date 2018-12-02
 */
@Data
@NoArgsConstructor
@ApiModel(description = "用户登录请求参数")
public class LoginParam implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	@NotNull(message = "用户名不能为空!")
	@Length(message = "用户名长度不能小于3位且不能大于25位!", min = 3, max = 25)
	@ApiModelProperty(value = "用户名", required = true)
	private String username;

	/**
	 * 密码
	 */
	@NotNull(message = "密码不能为空!")
	@Length(message = "密码长度不能小于8位且不能大于30位!", min = 8, max = 30)
	@ApiModelProperty(value = "密码", required = true)
	private String password;

}
