package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * 创建角色请求参数
 * @author Frodez
 * @date 2020-01-01
 */
@Data
@ValidateBean
@ApiModel(description = "创建角色请求参数")
public class CreateRole implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色名称
	 */
	@NotBlank
	@Length(max = 100)
	@ApiModelProperty("角色名称")
	private String name;

	/**
	 * 角色等级（0-9且0最高9最低）
	 */
	@NotNull
	@Range(min = 0, max = 9)
	@ApiModelProperty("角色等级（0-9且0最高9最低）")
	private Byte level;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty("描述")
	private String description;

	/**
	 * 菜单权限ID列表
	 */
	@ApiModelProperty("菜单权限ID列表")
	private List<Long> menuPermissions;

	/**
	 * 标签权限ID列表
	 */
	@ApiModelProperty("标签权限ID列表")
	private List<Long> tagPermissions;

}
