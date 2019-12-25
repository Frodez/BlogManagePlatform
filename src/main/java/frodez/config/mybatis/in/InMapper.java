package frodez.config.mybatis.in;

import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface InMapper<T> {

	/**
	 * 通过in条件查询,只支持一个in条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param params 该表中的对应字段
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = InMapperProvider.class, method = "dynamicSQL")
	List<T> selectIn(@Param("paramName") String paramName, @Param("params") List<?> params);

	/**
	 * 通过in条件查询,只支持一个in条件,是简单的封装。<br>
	 * 如果有多条结果,则会直接抛出异常。<br>
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param params 该表中的对应字段
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = InMapperProvider.class, method = "dynamicSQL")
	T selectOneIn(@Param("paramName") String paramName, @Param("params") List<?> params);

	/**
	 * 通过in条件删除,只支持一个in条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param params 该表中的对应字段
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@DeleteProvider(type = InMapperProvider.class, method = "dynamicSQL")
	int deleteIn(@Param("paramName") String paramName, @Param("params") List<?> params);

	/**
	 * 通过in条件更新所有字段,只支持一个in条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param params 该表中的对应字段
	 * @param record 更新的数据,为null的字段会更新为null
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@UpdateProvider(type = InMapperProvider.class, method = "dynamicSQL")
	int updateIn(@Param("paramName") String paramName, @Param("params") List<?> params, T record);

	/**
	 * 通过in条件更新所有record中不为null字段,只支持一个in条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param params 该表中的对应字段
	 * @param record 更新的数据,为null的字段将不更新
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@UpdateProvider(type = InMapperProvider.class, method = "dynamicSQL")
	int updateInSelective(@Param("paramName") String paramName, @Param("params") List<?> params, T record);

}
