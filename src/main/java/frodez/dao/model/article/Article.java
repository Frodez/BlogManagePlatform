package frodez.dao.model.article;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description 文章表
 * @table tb_article
 * @date 2019-03-06
 */
@Data
@Entity
@Table(name = "tb_article")
public class Article implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Id
	@NotNull
	@Column(name = "id")
	private Long id;

	/**
	 * 创建时间
	 */
	@NotNull
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@NotNull
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 用户ID
	 */
	@NotNull
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 可见角色最低等级
	 */
	@NotNull
	@Column(name = "permit_level")
	private Byte permitLevel;

	/**
	 * 是否被删除 1:未删除 2:已删除
	 */
	@NotNull
	@Column(name = "is_delete")
	private Byte isDelete = 1;

	/**
	 * 标题
	 */
	@NotNull
	@Column(name = "title", length = 127)
	private String title;

	/**
	 * 简介
	 */
	@Column(name = "description", length = 255)
	private String description;

	/**
	 * 标签
	 */
	@Column(name = "tag", length = 255)
	private String tag;

	/**
	 * 内容
	 */
	@NotNull
	@Column(name = "content", length = 65535)
	private String content;
}
