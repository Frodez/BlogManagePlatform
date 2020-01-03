package frodez.dao.mapper.permission;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.dao.model.table.permission.RoleMenu;
import org.springframework.stereotype.Repository;

/**
 * @description 角色与菜单对应表
 * @table tb_role_menu
 * @date 2019-12-31
 */
@Repository
public interface RoleMenuMapper extends DataMapper<RoleMenu> {
}