package frodez.dao.param.user;

import frodez.constant.settings.DefDesc;
import frodez.util.beans.param.QueryPage;
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
public class QueryRolePermission implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@NotNull
	@ApiModelProperty(value = "角色ID", required = true)
	private Long roleId;

	/**
	 * 分页查询参数
	 */
	@Valid
	@ApiModelProperty(value = DefDesc.Message.PAGE_QUERY)
	private QueryPage page;

}
