package frodez.dao.mapper.permission;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.config.mybatis.result.CustomResultHandler;
import frodez.config.mybatis.result.handler.MapResultHandler;
import frodez.dao.model.table.permission.Tag;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description 标签表
 * @table tb_tag
 * @date 2019-12-27
 */
@Repository
public interface TagMapper extends DataMapper<Tag> {

	/**
	 * 根据角色ID查询标签
	 * @author Frodez
	 * @date 2019-12-27
	 */
	List<Tag> getByRoleId(@Param("roleId") Long roleId);

	/**
	 * 根据角色ID批量查询标签
	 * @author Frodez
	 * @date 2019-12-27
	 */
	@CustomResultHandler(MapResultHandler.class)
	Map<Long, List<Tag>> batchGetByRoleId(@Param("roleIds") Collection<Long> roleIds);

}
