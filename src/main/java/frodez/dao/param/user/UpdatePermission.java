package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.constant.enums.user.PermissionTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 修改权限请求参数
 * @author Frodez
 * @date 2019-03-17
 */
@Data
@ValidateBean
@ApiModel(description = "修改权限请求参数")
public class UpdatePermission implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 权限ID
	 */
	@NotNull
	@ApiModelProperty(value = "权限ID")
	private Long id;

	/**
	 * 类型 0:ALL 1:GET 2:POST 3:DELETE 4:PUT
	 */
	@NotNull
	@LegalEnum(type = PermissionTypeEnum.class)
	@ApiModelProperty(value = "类型 0:ALL 1:GET 2:POST 3:DELETE 4:PUT")
	private Byte type;

	/**
	 * 权限名称
	 */
	@Length(max = 255)
	@ApiModelProperty(value = "权限名称")
	private String name;

	/**
	 * 地址
	 */
	@ApiModelProperty(value = "地址")
	private String url;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty(value = "描述")
	private String description;

}
