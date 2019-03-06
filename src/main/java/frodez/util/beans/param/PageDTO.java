package frodez.util.beans.param;

import frodez.constant.setting.DefPage;
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
public class PageDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_DESC = "分页查询参数";

	/**
	 * 合理化请求参数
	 * @author Frodez
	 * @date 2019-03-06
	 */
	public static PageDTO resonable(PageDTO page) {
		if (page == null) {
			return new PageDTO(DefPage.PAGE_NUM, DefPage.PAGE_SIZE);
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

}
