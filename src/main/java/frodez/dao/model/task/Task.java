package frodez.dao.model.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
	@Column(name = "id")
	@ApiModelProperty(value = "定时任务ID")
	private Long id;

	/**
	 * 创建时间(不能为空)
	 */
	@Column(name = "create_time")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 活跃状态 1:活跃中 2:不活跃(不能为空)
	 */
	@Column(name = "status")
	@ApiModelProperty(value = "活跃状态  1:活跃中  2:不活跃")
	private Byte status;

	/**
	 * 目标(不能为空)
	 */
	@Column(name = "target", length = 255)
	@ApiModelProperty(value = "目标")
	private String target;

	/**
	 * 任务描述(不能为空)
	 */
	@Column(name = "description", length = 255)
	@ApiModelProperty(value = "任务描述")
	private String description;

	/**
	 * cron表达式(不能为空)
	 */
	@Column(name = "cron_exp", length = 31)
	@ApiModelProperty(value = "cron表达式")
	private String cronExp;
}
