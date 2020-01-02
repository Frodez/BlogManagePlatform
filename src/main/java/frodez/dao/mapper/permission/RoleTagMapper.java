package frodez.dao.mapper.permission;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.dao.model.table.permission.RoleTag;
import org.springframework.stereotype.Repository;

/**
 * @description 角色与标签对应表
 * @table tb_role_tag
 * @date 2019-12-31
 */
@Repository
public interface RoleTagMapper extends DataMapper<RoleTag> {
}