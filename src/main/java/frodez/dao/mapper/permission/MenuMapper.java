package frodez.dao.mapper.permission;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.config.mybatis.result.CustomResultHandler;
import frodez.config.mybatis.result.handler.MapResultHandler;
import frodez.dao.model.table.permission.Menu;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description 菜单表
 * @table tb_menu
 * @date 2019-12-27
 */
@Repository
public interface MenuMapper extends DataMapper<Menu> {

	/**
	 * 根据角色ID查询菜单
	 * @author Frodez
	 * @date 2019-12-27
	 */
	List<Menu> getByRoleId(@Param("roleId") Long roleId);

	/**
	 * 根据角色ID批量查询菜单
	 * @author Frodez
	 * @date 2019-12-27
	 */
	@CustomResultHandler(MapResultHandler.class)
	Map<Long, List<Menu>> batchGetByRoleId(@Param("roleIds") Collection<Long> roleIds);

}
