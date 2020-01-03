package frodez.config.mybatis.mapper.ids;

import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface IdsMapper<T> {

	/**
	 * 通过id批量查询是否每个id都存在对应的值<br>
	 * <strong>如果每个id都存在对应值,则返回true,否则则返回false</strong>
	 * @param ids 必须是id的对应字段的值!!!
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = IdsMapperProvider.class, method = "dynamicSQL")
	boolean existByIds(@Param("ids") List<?> ids);

	/**
	 * 通过id批量查询
	 * @param ids 必须是id的对应字段的值!!!
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = IdsMapperProvider.class, method = "dynamicSQL")
	List<T> selectByIds(@Param("ids") List<?> ids);

	/**
	 * 通过id批量删除
	 * @param ids 必须是id的对应字段的值!!!
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@DeleteProvider(type = IdsMapperProvider.class, method = "dynamicSQL")
	int deleteByIds(@Param("ids") List<?> ids);

}
