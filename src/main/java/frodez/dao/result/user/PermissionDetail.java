package frodez.dao.result.user;

import frodez.config.swagger.annotation.EnumParam;
import frodez.constant.enums.user.PermissionTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 权限详细信息返回数据
 * @author Frodez
 * @date 2019-03-19
 */
@Data
@ApiModel(description = "权限详细信息返回数据")
public class PermissionDetail implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 权限ID
	 */
	@ApiModelProperty("权限ID")
	private Long id;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 类型 1:GET 2:POST 3:DELETE 4:PUT
	 */
	@EnumParam(PermissionTypeEnum.class)
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

	/**
	 * 拥有该权限的角色ID
	 */
	@ApiModelProperty("拥有该权限的角色ID")
	private List<Long> roleIds;

}
