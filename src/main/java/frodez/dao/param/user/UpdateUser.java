package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.aop.validation.annotation.common.AnyExist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 更新用户信息请求参数
 * @author Frodez
 * @date 2019-12-29
 */
@Data
@AnyExist({ "nickName", "email", "phone" })
@ValidateBean
@ApiModel(description = "更新用户信息请求参数")
public class UpdateUser implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@NotNull
	@ApiModelProperty("用户ID")
	private Long id;

	/**
	 * 昵称
	 */
	@Length(max = 50)
	@ApiModelProperty("昵称")
	private String nickname;

	/**
	 * 邮箱地址
	 */
	@Length(max = 255)
	@ApiModelProperty("邮箱地址")
	private String email;

	/**
	 * 电话号码
	 */
	@Length(max = 255)
	@ApiModelProperty("电话号码")
	private String phone;

}
