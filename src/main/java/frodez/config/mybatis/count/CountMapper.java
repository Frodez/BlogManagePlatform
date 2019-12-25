package frodez.config.mybatis.count;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface CountMapper {

	/**
	 * 统计所有
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = CountMapperProvider.class, method = "dynamicSQL")
	int countAll();

	/**
	 * 通过id批量统计
	 * @param ids 必须是id的对应字段的值!!!
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = CountMapperProvider.class, method = "dynamicSQL")
	int countByIds(@Param("ids") List<?> ids);

	/**
	 * 通过equal条件统计,只支持一个equal条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param param 该表中的对应字段
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = CountMapperProvider.class, method = "dynamicSQL")
	int countEqual(@Param("paramName") String paramName, @Param("param") Object param);

	/**
	 * 通过in条件统计,只支持一个in条件,是简单的封装
	 * @param paramName 该表中的字段名(和Example不同,只能是表中字段名,不能是实体对应字段名)
	 * @param params 该表中的对应字段
	 * @author Frodez
	 * @date 2019-12-25
	 */
	@SelectProvider(type = CountMapperProvider.class, method = "dynamicSQL")
	int countIn(@Param("paramName") String paramName, @Param("params") List<?> params);

}
