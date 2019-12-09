package frodez.dao.param.task;

import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.constant.enums.task.StartNowEnum;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import io.swagger.annotations.ApiModelProperty;

/**
 * 新增定时任务请求参数
 * @author Frodez
 * @date 2019-03-20
 */
@Data
@ValidateBean
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
	@ApiModelProperty("目标")
	private String target;

	/**
	 * 任务描述
	 */
	@NotBlank
	@Length(max = 65535)
	@ApiModelProperty("任务描述")
	private String description;

	/**
	 * cron表达式
	 */
	@NotBlank
	@Length(max = 31)
	@ApiModelProperty("cron表达式")
	private String cronExp;

	/**
	 * 是否立刻启动 1:立刻启动 2:暂不启动
	 */
	@NotNull
	@MapEnum(StartNowEnum.class)
	private Byte startNow;

}
