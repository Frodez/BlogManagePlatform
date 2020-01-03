package frodez.dao.model.result.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 重新登录返回数据
 * @author Frodez
 * @date 2019-12-26
 */
@Data
@ApiModel(description = "重新登录返回数据")
public class RefreshInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 新token
	 */
	@NotBlank
	@ApiModelProperty("新token")
	private String newToken;

	/**
	 * 重定向地址
	 */
	@NotBlank
	@ApiModelProperty("重定向地址")
	private String redirect;

}
