package frodez.util.result;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 分页查询数据,所有分页查询接口均需使用此类型作为包装.
 * @author Frodez
 * @date 2019-01-13
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询总数
	 */
	@Getter
	private int total;

	/**
	 * 分页数据
	 */
	@Getter
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
