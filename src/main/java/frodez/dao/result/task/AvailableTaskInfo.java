package frodez.dao.result.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 可用任务信息
 * @author Frodez
 * @date 2019-03-21
 */
@Data
@ApiModel(description = "可用任务信息返回数据")
public class AvailableTaskInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	@NotBlank
	@ApiModelProperty("名称")
	private String name;

	/**
	 * 描述
	 */
	@NotBlank
	@Length(max = 65535)
	@ApiModelProperty("描述")
	private String description;

	/**
	 * 可被强制中断(只是建议,非必要)
	 */
	@NotNull
	@ApiModelProperty("可被强制中断(只是建议,非必要)")
	private Boolean permitForceInterrupt;

}
