package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.aop.validation.annotation.common.AnyExist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * 更新角色请求参数
 * @author Frodez
 * @date 2020-01-01
 */
@Data
@ValidateBean
@AnyExist({ "name", "level", "description" })
@ApiModel(description = "更新角色请求参数")
public class UpdateRole implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@NotNull
	@ApiModelProperty("角色ID")
	private Long id;

	/**
	 * 角色名称
	 */
	@Length(max = 100)
	@ApiModelProperty("角色名称")
	private String name;

	/**
	 * 角色等级（0-9且0最高9最低）
	 */
	@Range(min = 0, max = 9)
	@ApiModelProperty("角色等级（0-9且0最高9最低）")
	private Byte level;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty("描述")
	private String description;

}
