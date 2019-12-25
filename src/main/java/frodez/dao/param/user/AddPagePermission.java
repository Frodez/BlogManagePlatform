package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.ValidateBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 新增页面资源权限请求参数
 * @author Frodez
 * @date 2019-12-25
 */
@Data
@ValidateBean
@ApiModel(description = "新增页面资源权限请求参数")
public class AddPagePermission implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

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
