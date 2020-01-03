package frodez.dao.model.result.user;

import frodez.dao.model.result.permission.PermissionDetail;
import frodez.dao.model.table.user.Role;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户详细信息
 * @author Frodez
 * @date 2019-12-27
 */
@Data
@ApiModel(description = "用户详细信息")
public class UserDetail implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户信息
	 */
	@Valid
	@NotNull
	private UserBaseInfo user;

	/**
	 * 用户角色信息
	 */
	@Valid
	@NotNull
	private Role role;

	/**
	 * 用户权限详细信息
	 */
	@Valid
	@NotNull
	private PermissionDetail permission;

}
