package frodez.dao.model.table.task;

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

/**
 * @description 定时任务表
 * @table tb_spring_task
 * @date 2019-12-09
 */
@Data
@Entity
@Table(name = "tb_spring_task")
@ApiModel(description = "定时任务信息")
public class Task implements Serializable {

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
	 * 运行状态 1:活跃中 2:已暂停(不能为空)
	 */
	@NotNull
	@Column(name = "status")
	@ApiModelProperty("运行状态  1:活跃中  2:已暂停")
	private Byte status;

	/**
	 * 目标(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@Column(name = "target", length = 255)
	@ApiModelProperty("目标")
	private String target;

	/**
	 * 任务描述(不能为空)
	 */
	@NotBlank
	@Length(max = 255)
	@Column(name = "description", length = 255)
	@ApiModelProperty("任务描述")
	private String description;

	/**
	 * cron表达式(不能为空)
	 */
	@NotBlank
	@Length(max = 31)
	@Column(name = "cron_exp", length = 31)
	@ApiModelProperty("cron表达式")
	private String cronExp;
}
