package frodez.service.user.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description 用户角色表
 * @table tb_role
 * @date 2019-01-13
 */
@Data
@Entity
@Table(name = "tb_role")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Id
	@NotNull
	@Column(name = "id")
	private Long id;

	/**
	 * 创建时间
	 */
	@NotNull
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 角色名称
	 */
	@NotNull
	@Column(name = "name", length = 255)
	private String name;

	/**
	 * 角色等级 0-9 0最高,9最低
	 */
	@NotNull
	@Column(name = "level")
	private Byte level = 0;

	/**
	 * 描述
	 */
	@Column(name = "description", length = 1000)
	private String description;
}
