package frodez.dao.result.user;

import frodez.dao.model.user.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 页面资源权限详细信息返回数据
 * @author Frodez
 * @date 2019-12-25
 */
@Data
@ApiModel(description = "页面资源权限详细信息返回数据")
public class PagePermissionDetail implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 页面资源权限ID
	 */
	@NotNull
	@ApiModelProperty("页面资源权限ID")
	private Long id;

	/**
	 * 创建时间
	 */
	@NotNull
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 页面资源权限名称
	 */
	@NotBlank
	@Length(max = 100)
	@ApiModelProperty("页面资源权限名称")
	private String name;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty("描述")
	private String description;

	/**
	 * 拥有该权限的角色
	 */
	@ApiModelProperty("拥有该权限的角色")
	private List<Role> roles;

}
