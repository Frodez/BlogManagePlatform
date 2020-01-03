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
 * @description 标签表
 * @table tb_tag
 * @date 2019-12-27
 */
@Data
@Entity
@Table(name = "tb_tag")
@ApiModel(description = "标签信息")
public class Tag implements Serializable {

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
	 * 标签权限名(不能为空)
	 */
	@NotBlank
	@Length(max = 100)
	@Column(name = "permission_name", length = 100)
	@ApiModelProperty("标签权限名")
	private String permissionName;

	/**
	 * 权限类型 0:无权限隐藏 1:无权限不可用(不能为空,默认值:0)
	 */
	@NotNull
	@Column(name = "type")
	@ApiModelProperty("权限类型  0:无权限隐藏 1:无权限不可用")
	private Byte type = 0;

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
