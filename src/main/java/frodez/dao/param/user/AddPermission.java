package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.util.enums.user.PermissionTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 新增权限请求参数
 * @author Frodez
 * @date 2019-03-15
 */
@Data
@NoArgsConstructor
@ApiModel(description = "新增权限请求参数")
public class AddPermission implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 权限名称
	 */
	@NotBlank
	@Length(max = 100)
	@ApiModelProperty(value = "权限名称", required = true)
	private String name;

	/**
	 * 类型 0:ALL 1:GET 2:POST 3:DELETE 4:PUT(不能为空,默认值:0)
	 */
	@NotNull
	@LegalEnum(type = PermissionTypeEnum.class)
	@ApiModelProperty(value = "类型 0:ALL 1:GET 2:POST 3:DELETE 4:PUT(不能为空,默认值:0)", required = true)
	private Byte type;

	/**
	 * 地址
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty(value = "地址", required = true)
	private String url;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty(value = "描述")
	private String description;

}
