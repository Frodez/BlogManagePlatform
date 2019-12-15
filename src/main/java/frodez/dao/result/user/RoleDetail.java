package frodez.dao.result.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 角色详细信息返回数据
 * @author Frodez
 * @date 2019-03-19
 */
@Data
@ApiModel(description = "角色详细信息返回数据")
public class RoleDetail implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID(不能为空)
	 */
	@NotNull
	@ApiModelProperty("角色ID")
	private Long id;

	/**
	 * 创建时间(不能为空)
	 */
	@NotNull
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 角色名称(不能为空)
	 */
	@NotBlank
	@Length(min = 3, max = 255)
	@ApiModelProperty("角色名称")
	private String name;

	/**
	 * 角色等级 0-9 0最高,9最低(不能为空,默认值:0)
	 */
	@Min(0)
	@Max(9)
	@NotNull
	@ApiModelProperty(value = "角色等级  0-9  0最高,9最低")
	private Byte level = 0;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty("描述")
	private String description;

	/**
	 * 角色拥有的权限ID
	 */
	@ApiModelProperty("角色拥有的权限ID")
	private List<Long> permissionIds;

}
