package info.frodez.dao.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import info.frodez.config.mybatis.DataMapper;
import info.frodez.dao.model.user.Permission;
import info.frodez.dao.result.user.PermissionInfo;

/**
 * @description 用户权限表
 * @table tb_permission
 * @date 2018-11-14
 */
@Mapper
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
