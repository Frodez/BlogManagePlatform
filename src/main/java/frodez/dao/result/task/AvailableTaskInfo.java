package frodez.dao.result.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

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
	@ApiModelProperty("名称")
	private String name;

	/**
	 * 描述
	 */
	@ApiModelProperty("描述")
	private String description;

	/**
	 * 可被强制中断(只是建议,非必要)
	 */
	@ApiModelProperty("可被强制中断(只是建议,非必要)")
	private Boolean permitForceInterrupt;

}
