package frodez.dao.result.user;

import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.config.aop.validation.annotation.special.Mobile;
import frodez.constant.enums.user.UserStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户信息
 * @author Frodez
 * @date 2018-11-14
 */
@Data
@ApiModel(description = "用户信息返回数据")
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@NotNull
	@ApiModelProperty("用户ID")
	private Long id;

	/**
	 * 用户名
	 */
	@NotBlank
	@Length(min = 3, max = 25)
	@ApiModelProperty("用户名")
	private String name;

	/**
	 * 密码
	 */
	@NotBlank
	@Length(min = 8, max = 30)
	@ApiModelProperty("密码")
	private String password;

	/**
	 * 昵称
	 */
	@Length(min = 3, max = 25)
	@ApiModelProperty("昵称")
	private String nickname;

	/**
	 * 邮箱地址
	 */
	@Email
	@ApiModelProperty(value = "邮箱地址")
	private String email;

	/**
	 * 电话号码
	 */
	@Mobile
	@ApiModelProperty(value = "电话号码")
	private String phone;

	/**
	 * 用户状态 0:禁用 1:正常
	 */
	@NotNull
	@MapEnum(value = UserStatusEnum.class)
	private Byte status;

	/**
	 * 角色ID
	 */
	@NotNull
	@ApiModelProperty(value = "角色ID")
	private Long roleId;

	/**
	 * 角色名称
	 */
	@NotBlank
	@Length(min = 3, max = 255)
	@ApiModelProperty(value = "角色名称")
	private String roleName;

	/**
	 * 角色等级 0-9 0最高,9最低
	 */
	@Min(0)
	@Max(9)
	@NotNull
	@ApiModelProperty(value = "角色等级 0-9 0最高,9最低")
	private Byte roleLevel;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty(value = "描述")
	private String roleDescription;

	/**
	 * 权限列表
	 */
	private List<PermissionInfo> permissionList;

}
