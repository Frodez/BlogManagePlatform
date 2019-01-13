
package info.frodez.dao.mapper.user;

import info.frodez.config.mybatis.DataMapper;
import info.frodez.dao.model.user.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @description 用户角色表
 * @table tb_role
 * @date 2019-01-13
 */
@Mapper
@Repository
public interface RoleMapper extends DataMapper<Role> {
}