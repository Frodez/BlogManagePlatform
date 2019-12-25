package frodez.config.mybatis.equal;

import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface EqualMapper<T> {

	/**
	 * 通过equal条件查询,只支持一个equal条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = EqualMapperProvider.class, method = "dynamicSQL")
	List<T> selectEqual(@Param("paramName") String paramName, @Param("param") Object param);

	/**
	 * 通过equal条件查询,只支持一个equal条件,是简单的封装。<br>
	 * 如果有多条结果,则会直接抛出异常。<br>
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = EqualMapperProvider.class, method = "dynamicSQL")
	T selectOneEqual(@Param("paramName") String paramName, @Param("param") Object param);

	/**
	 * 通过equal条件删除,只支持一个equal条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@DeleteProvider(type = EqualMapperProvider.class, method = "dynamicSQL")
	int deleteEqual(@Param("paramName") String paramName, @Param("param") Object param);

	/**
	 * 通过equal条件更新所有字段,只支持一个equal条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @param record 更新的数据,为null的字段会更新为null
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@UpdateProvider(type = EqualMapperProvider.class, method = "dynamicSQL")
	int updateEqual(@Param("paramName") String paramName, @Param("param") Object param, T record);

	/**
	 * 通过equal条件更新所有record中不为null字段,只支持一个equal条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @param record 更新的数据,为null的字段将不更新
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@UpdateProvider(type = EqualMapperProvider.class, method = "dynamicSQL")
	int updateEqualSelective(@Param("paramName") String paramName, @Param("param") Object param, T record);

}
