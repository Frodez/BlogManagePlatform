package frodez.dao.result.article;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章信息
 * @author Frodez
 * @date 2019-03-06
 */
@Data
@NoArgsConstructor
public class ArticleInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 作者
	 */
	private String authorName;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 简介
	 */
	private String description;

	/**
	 * 标签
	 */
	private List<String> tags;

	/**
	 * 内容
	 */
	private String content;

}
