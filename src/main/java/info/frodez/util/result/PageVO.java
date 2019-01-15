package info.frodez.util.result;

import java.io.Serializable;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询数据,所有分页查询接口均需使用此类型作为包装.
 * @author Frodez
 * @date 2019-01-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询总数
	 */
	private int total;

	/**
	 * 分页数据
	 */
	private Collection<T> page;

}
