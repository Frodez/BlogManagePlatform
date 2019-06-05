package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.constant.enums.common.ModifyEnum;
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
	@ApiModelProperty(value = "角色ID", required = true)
	private Long roleId;

	/**
	 * 操作类型 1:新增 2:删除 3:修改
	 */
	@NotNull
	@LegalEnum(type = ModifyEnum.class)
	@ApiModelProperty(value = "操作类型  1:新增  2:删除  3:修改", required = true)
	private Byte operationType;

	/**
	 * 权限ID
	 */
	@Valid
	@ApiModelProperty(value = "权限ID")
	private List<Long> permissionIds;

}
