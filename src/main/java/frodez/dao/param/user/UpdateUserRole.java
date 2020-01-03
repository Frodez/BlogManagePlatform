package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新用户角色信息请求参数
 * @author Frodez
 * @date 2020-01-02
 */
@Data
@ValidateBean
@ApiModel(description = "更新用户角色信息请求参数")
public class UpdateUserRole implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty
	@ApiModelProperty("用户ID")
	private List<Long> userIds;

	@NotNull
	@ApiModelProperty("角色ID")
	private Long roleId;

}
