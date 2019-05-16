package frodez.dao.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 用户重新登录请求参数
 * @author Frodez
 * @date 2019-02-27
 */
@Data
@NoArgsConstructor
@ApiModel(description = "用户重新登录请求参数")
public class DoRefresh implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	@NotBlank
	@Length(min = 3, max = 25)
	@ApiModelProperty(value = "用户名", required = true)
	private String username;

	/**
	 * 原token
	 */
	@NotBlank
	@ApiModelProperty(value = "原token", required = true)
	private String oldToken;

}
