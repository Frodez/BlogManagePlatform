package frodez.dao.param.config;

import frodez.config.aop.validation.annotation.ValidateBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 更新全局设置信息请求参数
 * @author Frodez
 * @date 2020-01-01
 */
@Data
@ValidateBean
@ApiModel(description = "更新全局设置信息请求参数")
public class UpdateGlobalData implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty("字段名")
	private String name;

	@NotNull
	@Min(1)
	@Max(4)
	@ApiModelProperty("字段类型 1:bool 2:int 3:double 4:string")
	private Byte type;

	@NotNull
	@Length(max = 1000)
	@ApiModelProperty("字段内容")
	private String content;

}
