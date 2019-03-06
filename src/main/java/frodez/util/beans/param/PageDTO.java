package frodez.util.beans.param;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 通用分页查询请求参数
 * @author Frodez
 * @date 2019-03-06
 */
@Data
@AllArgsConstructor
public class PageDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "页码数不能为空!")
	@Positive(message = "页码数不能为负数!")
	private Integer pageNum;

	@NotNull(message = "单页容量不能为空!")
	@Positive(message = "单页容量不能为负数!")
	private Integer pageSize;

	public static PageDTO resonable(PageDTO page) {
		if (page != null) {
			return page;
		}
		return new PageDTO(1, 0);
	}

}
