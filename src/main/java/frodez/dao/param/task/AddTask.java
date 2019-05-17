package frodez.dao.param.task;

import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.constant.enums.task.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 新增定时任务请求参数
 * @author Frodez
 * @date 2019-03-20
 */
@Data
@NoArgsConstructor
@ApiModel(description = "新增定时任务请求参数")
public class AddTask implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 目标
	 */
	@NotBlank
	@Length(max = 255)
	@ApiModelProperty(value = "目标", required = true)
	private String target;

	/**
	 * 任务描述
	 */
	@NotBlank
	@Length(max = 65535)
	@ApiModelProperty(value = "任务描述", required = true)
	private String description;

	/**
	 * cron表达式
	 */
	@NotBlank
	@Length(max = 31)
	@ApiModelProperty(value = "cron表达式", required = true)
	private String cronExp;

	/**
	 * 是否立刻启动 1:立刻启动 2:暂不启动
	 */
	@NotNull
	@LegalEnum(type = StatusEnum.class)
	@ApiModelProperty(value = "是否立刻启动 1:立刻启动 2:暂不启动", required = true)
	private Byte startNow;

}
