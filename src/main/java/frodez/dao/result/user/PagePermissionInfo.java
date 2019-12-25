package frodez.dao.result.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 页面资源权限信息
 * @author Frodez
 * @date 2019-12-24
 */
@Data
@ApiModel(description = "页面资源权限信息返回数据")
public class PagePermissionInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 页面资源权限ID
	 */
	@NotNull
	@ApiModelProperty("页面资源权限ID")
	private Long id;

	/**
	 * 页面资源权限名称
	 */
	@NotBlank
	@Length(max = 100)
	@ApiModelProperty("页面资源权限名称")
	private String name;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	@ApiModelProperty("描述")
	private String description;

}
