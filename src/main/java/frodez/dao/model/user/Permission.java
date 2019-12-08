package frodez.dao.model.user;

import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.constant.enums.user.PermissionTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * @description 用户权限表
 * @table tb_permission
 * @date 2019-03-15
 */
@Data
@Entity
@Table(name = "tb_permission")
@ApiModel(description = "用户权限返回数据")
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户权限ID(不能为空)
	 */
	@Id
	@NotNull
	@Column(name = "id")
	@ApiModelProperty("用户权限ID")
	private Long id;

	/**
	 * 创建时间(不能为空)
	 */
	@NotNull
	@Column(name = "create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 类型 0:ALL 1:GET 2:POST 3:DELETE 4:PUT(不能为空,默认值:0)
	 */
	@NotNull
	@Column(name = "type")
	@LegalEnum(PermissionTypeEnum.class)
	private Byte type = 0;

	/**
	 * 权限名称(不能为空)
	 */
	@NotNull
	@Column(name = "name", length = 100)
	@ApiModelProperty("权限名称")
	private String name;

	/**
	 * 地址(不能为空)
	 */
	@NotNull
	@Column(name = "url", length = 255)
	@ApiModelProperty("地址")
	private String url;

	/**
	 * 描述
	 */
	@Nullable
	@Column(name = "description", length = 1000)
	@ApiModelProperty("描述")
	private String description;

}
