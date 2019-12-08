package frodez.dao.model.task;

import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.constant.enums.task.StatusEnum;
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

/**
 * @description 定时任务表
 * @table tb_spring_task
 * @date 2019-03-20
 */
@Data
@Entity
@Table(name = "tb_spring_task")
@ApiModel(description = "定时任务返回数据")
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 定时任务ID(不能为空)
	 */
	@Id
	@NotNull
	@Column(name = "id")
	@ApiModelProperty("定时任务ID")
	private Long id;

	/**
	 * 创建时间(不能为空)
	 */
	@NotNull
	@Column(name = "create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 活跃状态 1:活跃中 2:不活跃(不能为空)
	 */
	@NotNull
	@Column(name = "status")
	@LegalEnum(StatusEnum.class)
	private Byte status;

	/**
	 * 目标(不能为空)
	 */
	@NotNull
	@Column(name = "target", length = 255)
	@ApiModelProperty("目标")
	private String target;

	/**
	 * 任务描述(不能为空)
	 */
	@NotNull
	@Column(name = "description", length = 255)
	@ApiModelProperty("任务描述")
	private String description;

	/**
	 * cron表达式(不能为空)
	 */
	@NotNull
	@Column(name = "cron_exp", length = 31)
	@ApiModelProperty("cron表达式")
	private String cronExp;

}
