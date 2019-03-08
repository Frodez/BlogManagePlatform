package frodez.dao.result.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "文章信息返回数据")
public class ArticleInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;

	/**
	 * 作者
	 */
	@ApiModelProperty(value = "作者")
	private String authorName;

	/**
	 * 标题
	 */
	@ApiModelProperty(value = "标题")
	private String title;

	/**
	 * 简介
	 */
	@ApiModelProperty(value = "简介")
	private String description;

	/**
	 * 标签
	 */
	@ApiModelProperty(value = "标签")
	private List<String> tags;

	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容")
	private String content;

}
