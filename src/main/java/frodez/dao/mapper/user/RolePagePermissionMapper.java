package frodez.dao.mapper.user;

import frodez.config.mybatis.DataMapper;
import frodez.dao.model.user.RolePagePermission;
import org.springframework.stereotype.Repository;

/**
 * @description 角色页面资源权限表
 * @table tb_role_page_permission
 * @date 2019-12-24
 */
@Repository
public interface RolePagePermissionMapper extends DataMapper<RolePagePermission> {
}