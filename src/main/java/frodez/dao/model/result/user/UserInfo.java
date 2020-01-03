package frodez.dao.model.result.user;

import frodez.dao.model.table.user.Role;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户信息
 * @author Frodez
 * @date 2019-12-29
 */
@Data
@ApiModel(description = "用户信息")
public class UserInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户基本信息
	 */
	@Valid
	@NotNull
	private UserBaseInfo user;

	/**
	 * 角色信息
	 */
	@Valid
	@NotNull
	private Role role;

}
