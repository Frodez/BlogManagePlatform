package frodez.dao.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 新增角色请求参数
 * @author Frodez
 * @date 2019-03-15
 */
@Data
@NoArgsConstructor
@ApiModel(description = "新增角色请求参数")
public class AddRole implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色名称
	 */
	@Length(max = 255)
	@NotBlank(message = "角色名称不能为空！")
	@ApiModelProperty(value = "角色名称", required = true)
	private String name;

	/**
	 * 角色等级 0-9 0最高,9最低
	 */
	@NotNull(message = "角色等级不能为空！")
	@Min(value = 0, message = "角色等级最小值为0！")
	@Max(value = 9, message = "角色等级最大值为9！")
	@ApiModelProperty(value = "角色等级 0-9 0最高,9最低", required = true)
	private Byte level;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty(value = "描述")
	private String description;

	/**
	 * 权限ID
	 */
	@ApiModelProperty(value = "权限ID")
	private List<Long> permissionIds;

}
