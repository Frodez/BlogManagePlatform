package frodez.dao.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
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
@ApiModel(value = "用户重新登录请求参数", description = "用户重新登录请求参数")
public class RefreshDTO implements Serializable {

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
	private String username;

	/**
	 * 原token
	 */
	@NotNull(message = "原token不能为空!")
	@ApiModelProperty(value = "原token", required = true)
	private String oldToken;

}
