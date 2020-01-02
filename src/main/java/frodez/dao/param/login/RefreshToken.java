package frodez.dao.param.login;

import frodez.config.aop.validation.annotation.ValidateBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户重新登录请求参数
 * @author Frodez
 * @date 2019-02-27
 */
@Data
@ValidateBean
@ApiModel(description = "用户重新登录请求参数")
public class RefreshToken implements Serializable {

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
	private String username;

	/**
	 * 原token
	 */
	@NotBlank
	@ApiModelProperty("原token")
	private String oldToken;

	/**
	 * 重定向地址
	 */
	@NotBlank
	@ApiModelProperty("重定向地址")
	private String redirect;

}
