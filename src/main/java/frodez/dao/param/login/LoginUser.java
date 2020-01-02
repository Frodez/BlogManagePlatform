package frodez.dao.param.login;

import frodez.config.aop.validation.annotation.ValidateBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户登录请求参数
 * @author Frodez
 * @date 2018-12-02
 */
@Data
@ValidateBean
@ApiModel(description = "用户登录请求参数")
public class LoginUser implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	@NotBlank
	@Length(min = 3, max = 25)
	@ApiModelProperty("用户名")
	private String username;

	/**
	 * 密码
	 */
	@NotBlank
	@Length(min = 8, max = 30)
	@ApiModelProperty("密码")
	private String password;

}
