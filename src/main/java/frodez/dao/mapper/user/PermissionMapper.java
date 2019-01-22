package frodez.dao.mapper.user;

import frodez.config.mybatis.DataMapper;
import frodez.dao.model.user.Permission;
import frodez.dao.result.user.PermissionInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description 用户权限表
 * @table tb_permission
 * @date 2019-01-13
 */
@Repository
public interface PermissionMapper extends DataMapper<Permission> {

	/**
	 * 获取权限信息
	 * @author Frodez
	 * @param roleId 角色ID
	 * @date 2018-11-14
	 */
	List<PermissionInfo> getPermissions(@Param("roleId") Long roleId);

}
