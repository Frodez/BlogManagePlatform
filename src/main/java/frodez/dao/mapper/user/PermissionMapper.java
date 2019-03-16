package frodez.dao.mapper.user;

import frodez.config.mybatis.DataMapper;
import frodez.dao.model.user.Permission;
import org.springframework.stereotype.Repository;

/**
 * @description 用户权限表
 * @table tb_permission
 * @date 2019-01-13
 */
@Repository
public interface PermissionMapper extends DataMapper<Permission> {

}
