package frodez.util.beans.param;

import com.github.pagehelper.IPage;
import frodez.util.constant.setting.DefDesc;
import frodez.util.constant.setting.DefPage;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 通用分页查询请求参数
 * @author Frodez
 * @date 2019-03-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(description = DefDesc.Message.PAGE_QUERY)
public class PageQuery implements IPage, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final PageQuery DEFAULT = new PageQuery(DefPage.PAGE_NUM, DefPage.PAGE_SIZE);

	/**
	 * 合理化请求参数
	 * @author Frodez
	 * @date 2019-03-06
	 */
	public static PageQuery resonable(PageQuery page) {
		if (page == null) {
			return PageQuery.DEFAULT;
		}
		return page;
	}

	@NotNull(message = "页码数不能为空!")
	@Positive(message = "页码数不能小于等于0!")
	private Integer pageNum;

	@NotNull(message = "单页容量不能为空!")
	@Positive(message = "单页容量不能小于等于0!")
	@Max(value = DefPage.MAX_PAGE_SIZE, message = "单页容量不能大于" + DefPage.MAX_PAGE_SIZE + "!")
	private Integer pageSize;

	/**
	 * <strong>警告!不要使用本方法!本方法永远返回null!</strong><br>
	 * <strong>请使用其他方式设置orderBy!<strong>
	 * @author Frodez
	 * @date 2019-03-10
	 */
	@Override
	public String getOrderBy() {
		return null;
	}

}
