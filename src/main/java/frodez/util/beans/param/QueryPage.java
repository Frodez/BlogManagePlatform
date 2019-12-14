package frodez.util.beans.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import frodez.constant.settings.DefDesc;
import frodez.constant.settings.DefPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.ibatis.session.RowBounds;

/**
 * 通用分页查询请求参数<br>
 * 本参数用于外部接口通信,出于安全考虑,限制了参数的取值范围。<br>
 * 如需无限制地进行分页查询,请使用RowBounds。<br>
 * @see org.apache.ibatis.session#RowBounds
 * @author Frodez
 * @date 2019-03-06
 */
@ToString
@EqualsAndHashCode
@ApiModel(description = DefDesc.Message.PAGE_QUERY)
public class QueryPage implements IPage, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 使用默认值
	 * @see frodez.constant.settings.DefPage#QUERY_PAGE
	 * @see frodez.constant.settings.DefPage#NUM
	 * @see frodez.constant.settings.DefPage#SIZE
	 */
	public QueryPage() {
		this.pageNum = DefPage.NUM;
		this.pageSize = DefPage.SIZE;
	}

	/**
	 * 构造函数<br>
	 * pageNum为默认值
	 * @see frodez.constant.settings.DefPage#NUM
	 * @param pageSize
	 */
	public QueryPage(Integer pageSize) {
		this.pageNum = DefPage.NUM;
		this.pageSize = pageSize;
	}

	/**
	 * 构造函数
	 * @param pageNum
	 * @param pageSize
	 */
	public QueryPage(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	/**
	 * 页码数
	 */
	@Getter
	@NotNull
	@Min(0)
	@ApiModelProperty(value = "页码数", example = "5")
	private Integer pageNum;

	/**
	 * 单页容量
	 */
	@Getter
	@NotNull
	@Min(1)
	@Max(DefPage.MAX_SIZE)
	@ApiModelProperty(value = "单页容量", example = "20")
	private Integer pageSize;

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

	/**
	 * 转换为RowBounds
	 * @author Frodez
	 * @date 2019-06-17
	 */
	public RowBounds toRowBounds() {
		return new RowBounds((pageNum - 1) * pageSize, pageSize);
	}

	/**
	 * 开始分页查询<br>
	 * <strong>请严格遵循PageHelper插件的使用方式!</strong>
	 * @see <url>https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md</url>
	 * @author Frodez
	 * @param <E>
	 * @date 2019-12-09
	 */
	public <E> Page<E> start(Supplier<List<E>> supplier) {
		PageHelper.startPage(this);
		return (Page<E>) supplier.get();
	}

}
