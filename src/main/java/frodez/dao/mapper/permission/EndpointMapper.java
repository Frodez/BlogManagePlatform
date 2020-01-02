package frodez.dao.mapper.permission;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.config.mybatis.result.CustomResultHandler;
import frodez.config.mybatis.result.handler.MapResultHandler;
import frodez.dao.model.table.permission.Endpoint;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description 接口表
 * @table tb_endpoint
 * @date 2019-12-27
 */
@Repository
public interface EndpointMapper extends DataMapper<Endpoint> {

	/**
	 * 根据菜单ID查询所有接口
	 * @author Frodez
	 * @date 2019-12-27
	 */
	List<Endpoint> getByMenuId(@Param("menuId") Long menuId);

	/**
	 * 根据标签ID查询所有接口
	 * @author Frodez
	 * @date 2019-12-27
	 */
	List<Endpoint> getByTagId(@Param("tagId") Long tagId);

	/**
	 * 根据菜单ID批量查询所有接口
	 * @author Frodez
	 * @date 2019-12-27
	 */
	@CustomResultHandler(MapResultHandler.class)
	Map<Long, List<Endpoint>> batchGetByMenuId(@Param("menuIds") Collection<Long> menuIds);

	/**
	 * 根据标签ID批量查询所有接口
	 * @author Frodez
	 * @date 2019-12-27
	 */
	@CustomResultHandler(MapResultHandler.class)
	Map<Long, List<Endpoint>> batchGetByTagId(@Param("tagIds") Collection<Long> tagIds);

	/**
	 * 根据角色ID获取菜单权限对应的所有接口
	 * @author Frodez
	 * @date 2019-12-29
	 */
	List<Endpoint> getMenuEndpoints(@Param("roleId") Long roleId);

	/**
	 * 根据角色ID获取标签权限对应的所有接口
	 * @author Frodez
	 * @date 2019-12-29
	 */
	List<Endpoint> getTagEndpoints(@Param("roleId") Long roleId);

}
