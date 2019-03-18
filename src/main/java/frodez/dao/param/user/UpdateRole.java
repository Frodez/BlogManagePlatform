package frodez.dao.param.user;

import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 修改角色请求参数
 * @author Frodez
 * @date 2019-03-17
 */
@Data
@NoArgsConstructor
@ApiModel(description = "修改角色请求参数")
public class UpdateRole implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@NotNull(message = "角色ID不能为空!")
	@ApiModelProperty(value = "角色ID", required = true)
	private Long id;

	/**
	 * 角色名称
	 */
	@Length(max = 255)
	@ApiModelProperty(value = "角色名称")
	private String name;

	/**
	 * 角色等级 0-9 0最高,9最低
	 */
	@Min(0)
	@Max(9)
	@ApiModelProperty(value = "角色等级 0-9 0最高,9最低")
	private Byte level;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty(value = "描述")
	private String description;

}
