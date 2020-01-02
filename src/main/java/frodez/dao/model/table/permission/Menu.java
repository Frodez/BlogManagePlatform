package frodez.dao.model.table.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

/**
 * @description 菜单表
 * @table tb_menu
 * @date 2019-12-27
 */
@Data
@Entity
@Table(name = "tb_menu")
@ApiModel(description = "菜单信息")
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID(不能为空)
	 */
	@Id
	@NotNull
	@Column(name = "id")
	@ApiModelProperty("ID")
	private Long id;

	/**
	 * 创建时间(不能为空)
	 */
	@NotNull
	@Column(name = "create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 菜单权限名(不能为空)
	 */
	@NotBlank
	@Length(max = 100)
	@Column(name = "permission_name", length = 100)
	@ApiModelProperty("菜单权限名")
	private String permissionName;

	/**
	 * 菜单名（为显示在菜单上的名称）(不能为空)
	 */
	@NotBlank
	@Length(max = 100)
	@Column(name = "name", length = 100)
	@ApiModelProperty("菜单名（为显示在菜单上的名称）")
	private String name;

	/**
	 * 路由路径(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@Column(name = "route", length = 255)
	@ApiModelProperty("路由路径")
	private String route;

	/**
	 * 上级菜单ID（为0即根菜单）(不能为空)
	 */
	@NotNull
	@Column(name = "parent_id")
	@ApiModelProperty("上级菜单ID（为0即根菜单）")
	private Long parentId;

	/**
	 * 默认拥有权限角色最低等级（0-9且0最高9最低）(不能为空)
	 */
	@NotNull
	@Column(name = "default_level")
	@ApiModelProperty("默认拥有权限角色最低等级（0-9且0最高9最低）")
	private Byte defaultLevel;

	/**
	 * 描述
	 */
	@Nullable
	@Length(max = 1000)
	@Column(name = "description", length = 1000)
	@ApiModelProperty("描述")
	private String description;

	/**
	 * 配置用json
	 */
	@Nullable
	@Length(max = 1000)
	@Column(name = "setting", length = 1000)
	@ApiModelProperty("配置用json")
	private String setting;
}
