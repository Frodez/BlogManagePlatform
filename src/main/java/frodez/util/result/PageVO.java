package frodez.util.result;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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

	/**
	 * 转换成list
	 * @author Frodez
	 * @date 2019-02-13
	 */
	public List<T> list() throws ClassCastException {
		return (List<T>) page;
	}

	/**
	 * 转换成set
	 * @author Frodez
	 * @date 2019-02-13
	 */
	public Set<T> set() throws ClassCastException {
		return (Set<T>) page;
	}

}
