package frodez.dao.model.result.permission;

import frodez.dao.model.table.permission.Endpoint;
import frodez.dao.model.table.permission.Tag;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 标签权限详细信息
 * @author Frodez
 * @date 2019-12-27
 */
@Data
@ApiModel(description = "标签权限详细信息")
public class TagPermissionDetail implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 标签信息
	 */
	@Valid
	@NotNull
	private Tag tag;

	/**
	 * 接口信息
	 */
	private List<@Valid Endpoint> endpoints;

}
