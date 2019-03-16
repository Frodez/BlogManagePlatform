package frodez.util.beans.result;

import frodez.util.constant.setting.DefDesc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = DefDesc.Message.PAGE_VO)
public class PageData<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 页码数
	 */
	@Getter
	@ApiModelProperty(value = "页码数")
	private int pageNum;

	/**
	 * 单页容量
	 */
	@Getter
	@ApiModelProperty(value = "单页容量")
	private int pageSize;

	/**
	 * 查询总数
	 */
	@Getter
	@ApiModelProperty(value = "查询总数")
	private long total;

	/**
	 * 分页数据
	 */
	@Getter
	@ApiModelProperty(value = "分页数据")
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
