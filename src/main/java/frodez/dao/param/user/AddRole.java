package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 新增角色请求参数
 * @author Frodez
 * @date 2019-03-15
 */
@Data
@ValidateBean
@ApiModel(description = "新增角色请求参数")
public class AddRole implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色名称
	 */
	@NotBlank
	@Length(min = 3, max = 255)
	@ApiModelProperty("角色名称")
	private String name;

	/**
	 * 角色等级 0-9 0最高,9最低
	 */
	@Min(0)
	@Max(9)
	@NotNull
	@ApiModelProperty("角色等级 0-9 0最高,9最低")
	private Byte level;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty("描述")
	private String description;

	/**
	 * 权限ID
	 */
	@ApiModelProperty("权限ID")
	private List<Long> permissionIds;

}
