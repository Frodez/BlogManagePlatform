package info.frodez.util.result;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 分页查询数据,所有分页查询接口均需使用此类型作为包装.
 * @author Frodez
 * @date 2019-01-13
 */
@Data
public class PageQueryVO<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询总数
	 */
	private int total;

	/**
	 * 分页数据
	 */
	private List<T> list;

}
