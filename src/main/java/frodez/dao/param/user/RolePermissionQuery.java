package frodez.dao.param.user;

import frodez.constant.setting.DefDesc;
import frodez.util.beans.param.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 权限查询请求参数
 * @author Frodez
 * @date 2019-03-06
 */
@Data
@ApiModel(description = "权限查询请求参数")
public class RolePermissionQuery implements Serializable {

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
	@ApiModelProperty(value = DefDesc.Message.PAGE_QUERY)
	private PageQuery page;

}
