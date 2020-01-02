package frodez.dao.model.table.permission;

import frodez.config.aop.validation.annotation.special.Methods;
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
 * @description 接口表
 * @table tb_endpoint
 * @date 2019-12-27
 */
@Data
@Entity
@Table(name = "tb_endpoint")
@ApiModel(description = "接口信息")
public class Endpoint implements Serializable {

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
	 * 接口名(不能为空)
	 */
	@NotBlank
	@Length(max = 100)
	@Column(name = "name", length = 100)
	@ApiModelProperty("接口名")
	private String name;

	/**
	 * 访问路径（截去基本路径）(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@Column(name = "path", length = 255)
	@ApiModelProperty("访问路径（截去基本路径）")
	private String path;

	/**
	 * 访问方式（为比特位掩码，某位为0代表无，为1代表有，从低位到高位为GET,POST,PUT,DELETE,HEAD,PATCH,OPTIONS,TRACE）(不能为空)
	 */
	@NotNull
	@Methods
	@Column(name = "methods")
	@ApiModelProperty("访问方式（为比特位掩码，某位为0代表无，为1代表有，从低位到高位为GET,POST,PUT,DELETE,HEAD,PATCH,OPTIONS,TRACE）")
	private Short methods;

	/**
	 * 默认拥有权限角色最低等级（0-9且0最高9最低）(不能为空)
	 */
	@NotNull
	@Column(name = "default_level")
	@ApiModelProperty("默认拥有权限角色最低等级（0-9且0最高9最低）")
	private Byte defaultLevel;

	/**
	 * 描述
	 */
	@Nullable
	@Length(max = 1000)
	@Column(name = "description", length = 1000)
	@ApiModelProperty("描述")
	private String description;
}
