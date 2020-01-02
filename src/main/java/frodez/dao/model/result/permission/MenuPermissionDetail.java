package frodez.dao.model.result.permission;

import frodez.dao.model.table.permission.Endpoint;
import frodez.dao.model.table.permission.Menu;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 菜单权限详细信息
 * @author Frodez
 * @date 2019-12-27
 */
@Data
@ApiModel(description = "菜单权限详细信息")
public class MenuPermissionDetail implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单信息
	 */
	@Valid
	@NotNull
	private Menu menu;

	/**
	 * 接口信息
	 */
	private List<@Valid Endpoint> endpoints;

}
