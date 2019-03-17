package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.util.constant.common.OperationEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	 * 操作类型 1:新增 2:删除 3:修改 4:查询(不支持)
	 */
	@LegalEnum(message = "操作类型错误!", type = OperationEnum.class)
	@ApiModelProperty(value = "操作类型  1:新增  2:删除  3:修改  4:查询(不支持)", required = true)
	private Byte operationType;

	/**
	 * 权限ID
	 */
	@ApiModelProperty(value = "权限ID")
	private List<Long> permissionIds;

}
