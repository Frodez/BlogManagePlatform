package frodez.dao.model.result.permission;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import lombok.Data;

/**
 * 用户权限详细信息
 * @author Frodez
 * @date 2019-12-27
 */
@Data
@ApiModel(description = "用户权限详细信息")
public class PermissionDetail implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单权限详细信息
	 */
	private List<@Valid MenuPermissionDetail> menuPermissions;

	/**
	 * 标签权限详细信息
	 */
	private List<@Valid TagPermissionDetail> tagPermissions;

}
