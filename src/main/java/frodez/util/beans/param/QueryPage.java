package frodez.util.beans.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.IPage;
import frodez.config.aop.validation.annotation.special.ValidQueryPage;
import frodez.constant.settings.DefDesc;
import frodez.constant.settings.DefPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 通用分页查询请求参数
 * @author Frodez
 * @date 2019-03-06
 */
@ToString
@ValidQueryPage
@EqualsAndHashCode
@ApiModel(description = DefDesc.Message.PAGE_QUERY)
public class QueryPage implements IPage, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 默认QueryPage
	 */
	public static final QueryPage DEFAULT = new QueryPage(DefPage.PAGE_NUM, DefPage.PAGE_SIZE);

	/**
	 * 使用默认值
	 */
	public QueryPage() {
		this.pageNum = DefPage.PAGE_NUM;
		this.pageSize = DefPage.PAGE_SIZE;
	}

	/**
	 * 构造函数
	 * @param pageSize 必须大于0,空值会被转化为默认值.
	 */
	public QueryPage(Integer pageSize) {
		this.pageNum = DefPage.PAGE_NUM;
		this.pageSize = pageSize == null ? DefPage.PAGE_SIZE : pageSize;
	}

	/**
	 * 构造函数
	 * @param pageNum 必须大于0,空值会被转化为默认值.
	 * @param pageSize 必须大于0,空值会被转化为默认值.
	 */
	public QueryPage(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum == null ? DefPage.PAGE_NUM : pageNum;
		this.pageSize = pageSize == null ? DefPage.PAGE_SIZE : pageSize;
	}

	/**
	 * 安全化请求参数,强制将null转化为默认参数<br>
	 * <strong>说明:有时候需要非强制传入QueryPage参数以达到不同参数不同行为的目标。<br>
	 * 但PageHelper不能接受null作为Page参数，此时便可使用本方法。</strong><br>
	 * @author Frodez
	 * @date 2019-03-06
	 */
	public static QueryPage safe(QueryPage page) {
		return page == null ? QueryPage.DEFAULT : page;
	}

	/**
	 * 页码数
	 */
	@Getter
	@ApiModelProperty(value = "页码数,必须大于0", example = "5")
	private Integer pageNum;

	/**
	 * 单页容量
	 */
	@Getter
	@ApiModelProperty(value = "单页容量,必须大于0且小于限定值", example = "20")
	private Integer pageSize;

	/**
	 * 是否允许超额单页容量,内部参数,请不要暴露<br>
	 * 当设置为true时,不会检查单页容量上限。<br>
	 * @see frodez.constant.settings.DefPage#MAX_PAGE_SIZE
	 */
	@Getter
	@JsonIgnore
	private boolean permitOutSize = false;

	/**
	 * 允许超额单页容量,用于参数校验<br>
	 * 仅限于内部调用使用,请不要作为外部参数暴露,以防恶意攻击! <br>
	 * @see frodez.constant.settings.DefPage#MAX_PAGE_SIZE
	 * @author Frodez
	 * @date 2019-05-16
	 */
	public QueryPage ableOutSize() {
		this.permitOutSize = true;
		return this;
	}

	/**
	 * 允许超额单页容量,用于参数校验<br>
	 * 仅限于内部调用使用,请不要作为外部参数暴露,以防恶意攻击! <br>
	 * @see frodez.constant.settings.DefPage#MAX_PAGE_SIZE
	 * @author Frodez
	 * @date 2019-05-16
	 */
	public QueryPage disableOutSize() {
		this.permitOutSize = false;
		return this;
	}

	/**
	 * <strong>警告!不要使用本方法!本方法永远返回null!</strong><br>
	 * <strong>请使用其他方式设置orderBy!<strong>
	 * @author Frodez
	 * @date 2019-03-10
	 */
	@Override
	@JsonIgnore
	public String getOrderBy() {
		return null;
	}

}
