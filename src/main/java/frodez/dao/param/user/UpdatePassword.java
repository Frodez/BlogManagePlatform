package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 更新角色密码请求参数
 * @author Frodez
 * @date 2020-01-03
 */
@Data
@ValidateBean
@ApiModel(description = "更新角色密码请求参数")
public class UpdatePassword implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 旧密码
	 */
	@NotBlank
	@Length(max = 2000)
	private String oldPassword;

	/**
	 * 新密码
	 */
	@NotBlank
	@Length(max = 2000)
	private String newPassword;

}
