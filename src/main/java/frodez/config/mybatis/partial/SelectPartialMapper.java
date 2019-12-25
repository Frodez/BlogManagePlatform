package frodez.config.mybatis.partial;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface SelectPartialMapper {

	/**
	 * 无条件查询部分内容,是简单的封装<br>
	 * <strong>不负责判断返回值类型!!!</strong>
	 * @param fieldName 查询的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @author Frodez
	 * @param <R>
	 * @date 2019-12-25
	 */
	@SelectProvider(type = SelectPartialMapperProvider.class, method = "dynamicSQL")
	<R> List<R> partialNoCondition(@Param("fieldName") String fieldName);

	/**
	 * 通过equal条件查询部分内容,只支持一个equal条件,是简单的封装<br>
	 * <strong>不负责判断返回值类型!!!</strong>
	 * @param fieldName 查询的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @author Frodez
	 * @param <R>
	 * @date 2019-12-25
	 */
	@SelectProvider(type = SelectPartialMapperProvider.class, method = "dynamicSQL")
	<R> List<R> partialEqual(@Param("fieldName") String fieldName, @Param("paramName") String paramName, @Param("param") Object param);

	/**
	 * 通过equal条件查询部分内容,只支持一个equal条件,是简单的封装。<br>
	 * 如果有多条结果,则会直接抛出异常。<br>
	 * <strong>不负责判断返回值类型!!!</strong>
	 * @param fieldName 查询的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @author Frodez
	 * @param <R>
	 * @date 2019-12-25
	 */
	@SelectProvider(type = SelectPartialMapperProvider.class, method = "dynamicSQL")
	<R> R partialOneEqual(@Param("fieldName") String fieldName, @Param("paramName") String paramName, @Param("param") Object param);

	/**
	 * 通过in条件查询部分内容,只支持一个in条件,是简单的封装<br>
	 * <strong>不负责判断返回值类型!!!</strong>
	 * @param fieldName 查询的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @author Frodez
	 * @param <R>
	 * @date 2019-12-25
	 */
	@SelectProvider(type = SelectPartialMapperProvider.class, method = "dynamicSQL")
	<R> List<R> partialIn(@Param("fieldName") String fieldName, @Param("paramName") String paramName, @Param("params") List<?> params);

	/**
	 * 通过in条件查询部分内容,只支持一个in条件,是简单的封装。<br>
	 * 如果有多条结果,则会直接抛出异常。<br>
	 * <strong>不负责判断返回值类型!!!</strong>
	 * @param fieldName 查询的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @author Frodez
	 * @param <R>
	 * @date 2019-12-25
	 */
	@SelectProvider(type = SelectPartialMapperProvider.class, method = "dynamicSQL")
	<R> R partialOneIn(@Param("fieldName") String fieldName, @Param("paramName") String paramName, @Param("params") List<?> params);

	/**
	 * 通过id批量查询部分内容<br>
	 * <strong>不负责判断返回值类型!!!</strong>
	 * @param fieldName 查询的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param ids 必须是id的对应字段的值!!!
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = SelectPartialMapperProvider.class, method = "dynamicSQL")
	<R> List<R> partialByIds(@Param("fieldName") String fieldName, @Param("ids") List<?> ids);

	/**
	 * 通过example批量查询部分内容<br>
	 * <strong>不负责判断返回值类型!!!</strong>
	 * @param fieldName 查询的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = SelectPartialMapperProvider.class, method = "dynamicSQL")
	<R> List<R> partialByExample(@Param("fieldName") String fieldName, @Param("example") Object example);

}
