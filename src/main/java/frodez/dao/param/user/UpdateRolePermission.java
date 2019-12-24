package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.constant.enums.common.ModifyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改角色权限请求参数
 * @author Frodez
 * @date 2019-03-17
 */
@Data
@ValidateBean
@ApiModel(description = "修改角色权限请求参数")
public class UpdateRolePermission implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@NotNull
	@ApiModelProperty("角色ID")
	private Long roleId;

	/**
	 * 操作类型 1:新增 2:删除 3:修改
	 */
	@NotNull
	@MapEnum(ModifyType.class)
	private Byte operationType;

	/**
	 * 权限ID
	 */
	@Valid
	@ApiModelProperty("权限ID")
	private List<Long> permissionIds;

}
