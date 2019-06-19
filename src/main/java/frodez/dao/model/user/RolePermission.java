package frodez.dao.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description 角色权限表
 * @table tb_role_permission
 * @date 2019-03-15
 */
@Data
@Entity
@Table(name = "tb_role_permission")
@ApiModel(description = "角色权限返回数据")
public class RolePermission implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色权限ID(不能为空)
	 */
	@Id
	@NotNull
	@Column(name = "id")
	@ApiModelProperty(value = "角色权限ID")
	private Long id;

	/**
	 * 创建时间(不能为空)
	 */
	@NotNull
	@Column(name = "create_time")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 角色ID(不能为空)
	 */
	@NotNull
	@Column(name = "role_id")
	@ApiModelProperty(value = "角色ID")
	private Long roleId;

	/**
	 * 权限ID(不能为空)
	 */
	@NotNull
	@Column(name = "permission_id")
	@ApiModelProperty(value = "权限ID")
	private Long permissionId;

}
