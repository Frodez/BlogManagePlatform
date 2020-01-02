package frodez.dao.model.result.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 文章信息
 * @author Frodez
 * @date 2019-03-06
 */
@Data
@ApiModel(description = "文章")
public class ArticleInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	private Date updateTime;

	/**
	 * 作者
	 */
	@ApiModelProperty("作者")
	private String authorName;

	/**
	 * 标题
	 */
	@ApiModelProperty("标题")
	private String title;

	/**
	 * 简介
	 */
	@ApiModelProperty("简介")
	private String description;

	/**
	 * 标签
	 */
	@ApiModelProperty("标签")
	private List<String> tags;

	/**
	 * 内容
	 */
	@ApiModelProperty("内容")
	private String content;

}
