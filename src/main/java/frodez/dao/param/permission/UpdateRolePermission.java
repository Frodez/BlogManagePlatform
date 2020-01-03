package frodez.dao.param.permission;

import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.constant.enums.common.ModifyType;
import frodez.constant.enums.permission.PermissionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新角色权限信息请求参数
 * @author Frodez
 * @date 2019-12-31
 */
@Data
@ValidateBean
@ApiModel(description = "更新角色权限信息请求参数")
public class UpdateRolePermission implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色id
	 */
	@NotNull
	@ApiModelProperty("角色ID")
	private Long roleId;

	/**
	 * 更新类型
	 */
	@NotNull
	@MapEnum(ModifyType.class)
	@ApiModelProperty("更新类型")
	private Byte modifyType;

	/**
	 * 菜单类型
	 */
	@NotNull
	@MapEnum(PermissionType.class)
	@ApiModelProperty("菜单类型")
	private Byte permissionType;

	/**
	 * 权限ID
	 */
	@ApiModelProperty("权限ID")
	private List<Long> permissionIds;

}
