package frodez.dao.mapper.user;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.config.mybatis.result.CustomResultHandler;
import frodez.config.mybatis.result.handler.MapResultHandler;
import frodez.dao.model.table.user.Role;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description 角色表
 * @table tb_role
 * @date 2019-12-27
 */
@Repository
public interface RoleMapper extends DataMapper<Role> {

	/**
	 * 根据用户ID获取角色
	 * @author Frodez
	 * @date 2019-12-27
	 */
	Role getByUserId(@Param("userId") Long userId);

	/**
	 * 根据用户名获取角色
	 * @author Frodez
	 * @date 2019-12-27
	 */
	Role getByUserName(@Param("userName") String userName);

	/**
	 * 根据用户ID获取角色
	 * @author Frodez
	 * @date 2019-12-27
	 */
	@CustomResultHandler(MapResultHandler.class)
	Map<Long, Role> batchGetByUserId(@Param("userIds") Collection<Long> userIds);

	/**
	 * 根据用户名获取角色
	 * @author Frodez
	 * @date 2019-12-27
	 */
	@CustomResultHandler(MapResultHandler.class)
	Map<Long, Role> batchGetByUserName(@Param("userNames") Collection<String> userNames);

	/**
	 * 为角色添加菜单权限
	 * @author Frodez
	 * @date 2019-12-31
	 */
	int insertRoleMenuPermissions(Long roleId, List<Long> menuIds);

}
