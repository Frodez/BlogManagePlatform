package frodez.dao.model.table.user;

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
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

/**
 * @description 角色表
 * @table tb_role
 * @date 2019-12-27
 */
@Data
@Entity
@Table(name = "tb_role")
@ApiModel(description = "角色信息")
public class Role implements Serializable {

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
	 * 角色名称(不能为空)
	 */
	@NotBlank
	@Length(max = 100)
	@Column(name = "name", length = 100)
	@ApiModelProperty("角色名称")
	private String name;

	/**
	 * 角色等级（0-9且0最高9最低）(不能为空)
	 */
	@NotNull
	@Range(min = 0, max = 9)
	@Column(name = "level")
	@ApiModelProperty("角色等级（0-9且0最高9最低）")
	private Byte level;

	/**
	 * 描述
	 */
	@Nullable
	@Length(max = 1000)
	@Column(name = "description", length = 1000)
	@ApiModelProperty("描述")
	private String description;
}
