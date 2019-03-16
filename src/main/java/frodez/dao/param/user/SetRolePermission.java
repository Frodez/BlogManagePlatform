package frodez.dao.param.user;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 赋予角色权限请求参数
 * @author Frodez
 * @date 2019-03-17
 */
@Data
@NoArgsConstructor
@ApiModel(description = "赋予角色权限请求参数")
public class SetRolePermission implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@NotNull(message = "权限ID不能为空!")
	@ApiModelProperty(value = "角色ID", required = true)
	private Long roleId;

	/**
	 * 权限ID
	 */
	@ApiModelProperty(value = "权限ID")
	private List<Long> permissionIds;

}
