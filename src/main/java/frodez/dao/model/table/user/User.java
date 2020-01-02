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
import org.springframework.lang.Nullable;

/**
 * @description 用户表
 * @table tb_user
 * @date 2019-12-09
 */
@Data
@Entity
@Table(name = "tb_user")
@ApiModel(description = "用户信息")
public class User implements Serializable {

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
	 * 用户名(不能为空)
	 */
	@NotBlank
	@Length(max = 50)
	@Column(name = "name", length = 50)
	@ApiModelProperty("用户名")
	private String name;

	/**
	 * 密码(不能为空)
	 */
	@NotBlank
	@Length(max = 2000)
	@Column(name = "password", length = 2000)
	@ApiModelProperty("密码")
	private String password;

	/**
	 * 昵称
	 */
	@Nullable
	@Length(max = 50)
	@Column(name = "nickname", length = 50)
	@ApiModelProperty("昵称")
	private String nickname;

	/**
	 * 邮箱地址
	 */
	@Nullable
	@Length(max = 255)
	@Column(name = "email", length = 255)
	@ApiModelProperty("邮箱地址")
	private String email;

	/**
	 * 电话号码
	 */
	@Nullable
	@Length(max = 255)
	@Column(name = "phone", length = 255)
	@ApiModelProperty("电话号码")
	private String phone;

	/**
	 * 用户状态 0:禁用 1:正常(不能为空,默认值:1)
	 */
	@NotNull
	@Column(name = "status")
	@ApiModelProperty(value = "用户状态  0:禁用  1:正常")
	private Byte status = 1;

	/**
	 * 角色ID(不能为空)
	 */
	@NotNull
	@Column(name = "role_id")
	@ApiModelProperty("角色ID")
	private Long roleId;
}
