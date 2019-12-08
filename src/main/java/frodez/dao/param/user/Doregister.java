package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.aop.validation.annotation.special.Mobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户注册请求参数
 * @author Frodez
 * @date 2019-02-02
 */
@Data
@ValidateBean
@ApiModel(description = "用户注册请求参数")
public class Doregister implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	@NotBlank
	@Length(min = 3, max = 25)
	@ApiModelProperty("用户名")
	private String name;

	/**
	 * 密码
	 */
	@NotBlank
	@Length(min = 8, max = 30)
	@ApiModelProperty("密码")
	private String password;

	/**
	 * 昵称
	 */
	@Length(min = 3, max = 25)
	@ApiModelProperty("昵称")
	private String nickname;

	/**
	 * 邮箱地址
	 */
	@Email
	@ApiModelProperty("邮箱地址")
	private String email;

	/**
	 * 电话号码
	 */
	@Mobile
	@ApiModelProperty("电话号码")
	private String phone;

}
