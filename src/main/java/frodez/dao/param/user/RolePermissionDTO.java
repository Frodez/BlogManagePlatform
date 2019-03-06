package frodez.dao.param.user;

import frodez.util.beans.param.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 根据角色ID查询权限请求参数
 * @author Frodez
 * @date 2019-03-06
 */
@Data
public class RolePermissionDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@NotNull(message = "角色ID不能为空!")
	@ApiModelProperty(value = "角色ID", required = true)
	private Long roleId;

	/**
	 * 分页查询参数
	 */
	@Valid
	@ApiModelProperty(value = PageDTO.DEFAULT_DESC)
	private PageDTO page;

}
