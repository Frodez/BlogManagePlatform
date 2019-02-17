package frodez.dao.mapper.user;

import frodez.config.mybatis.DataMapper;
import frodez.dao.model.user.Role;
import org.springframework.stereotype.Repository;

/**
 * @description 用户角色表
 * @table tb_role
 * @date 2019-01-13
 */
@Repository
public interface RoleMapper extends DataMapper<Role> {
}
