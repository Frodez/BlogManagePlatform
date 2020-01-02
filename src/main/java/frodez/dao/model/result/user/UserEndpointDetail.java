package frodez.dao.model.result.user;

import frodez.dao.model.table.permission.Endpoint;
import frodez.dao.model.table.user.Role;
import frodez.dao.model.table.user.User;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户有权限访问接口详细信息
 * @author Frodez
 * @date 2019-12-27
 */
@Data
@ApiModel(description = "用户有权限访问接口详细信息")
public class UserEndpointDetail implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户信息
	 */
	@Valid
	@NotNull
	private User user;

	/**
	 * 用户角色信息
	 */
	@Valid
	@NotNull
	private Role role;

	/**
	 * 接口信息
	 */
	private List<@Valid Endpoint> endpoints;

}
