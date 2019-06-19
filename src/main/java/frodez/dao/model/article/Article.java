package frodez.dao.model.article;

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
import org.springframework.lang.Nullable;

/**
 * @description 文章表
 * @table tb_article
 * @date 2019-03-15
 */
@Data
@Entity
@Table(name = "tb_article")
@ApiModel(description = "文章返回数据")
public class Article implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 文章ID(不能为空)
	 */
	@Id
	@NotNull
	@Column(name = "id")
	@ApiModelProperty(value = "文章ID")
	private Long id;

	/**
	 * 创建时间(不能为空)
	 */
	@NotNull
	@Column(name = "create_time")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 修改时间(不能为空)
	 */
	@NotNull
	@Column(name = "update_time")
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;

	/**
	 * 作者ID(不能为空)
	 */
	@NotNull
	@Column(name = "user_id")
	@ApiModelProperty(value = "作者ID")
	private Long userId;

	/**
	 * 可见角色最低等级(不能为空)
	 */
	@NotNull
	@Column(name = "permit_level")
	@ApiModelProperty(value = "可见角色最低等级")
	private Byte permitLevel;

	/**
	 * 是否被删除 1:未删除 2:已删除(不能为空,默认值:1)
	 */
	@NotNull
	@Column(name = "is_delete")
	@ApiModelProperty(value = "是否被删除  1:未删除  2:已删除", example = "1")
	private Byte isDelete = 1;

	/**
	 * 标题(不能为空)
	 */
	@NotNull
	@Column(name = "title", length = 127)
	@ApiModelProperty(value = "标题")
	private String title;

	/**
	 * 简介
	 */
	@Nullable
	@Column(name = "description", length = 255)
	@ApiModelProperty(value = "简介")
	private String description;

	/**
	 * 标签
	 */
	@Nullable
	@Column(name = "tag", length = 255)
	@ApiModelProperty(value = "标签")
	private String tag;

	/**
	 * 内容(不能为空)
	 */
	@NotNull
	@Column(name = "content", length = 65535)
	@ApiModelProperty(value = "内容")
	private String content;

}
