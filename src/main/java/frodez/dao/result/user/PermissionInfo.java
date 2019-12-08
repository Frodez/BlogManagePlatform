package frodez.dao.result.user;

import frodez.config.swagger.annotation.EnumParam;
import frodez.constant.enums.user.PermissionTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * 权限信息
 * @author Frodez
 * @date 2018-11-14
 */
@Data
@ApiModel(description = "权限信息返回数据")
public class PermissionInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 权限ID
	 */
	@ApiModelProperty("权限ID")
	private Long id;

	/**
	 * 类型 1:GET 2:POST 3:DELETE 4:PUT
	 */
	@EnumParam(value = PermissionTypeEnum.class)
	private Byte type;

	/**
	 * 权限名称
	 */
	@ApiModelProperty("权限名称")
	private String name;

	/**
	 * 地址
	 */
	@ApiModelProperty("地址")
	private String url;

	/**
	 * 描述
	 */
	@ApiModelProperty("描述")
	private String description;

}
