package frodez.dao.mapper.permission;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.dao.model.table.permission.MenuEndpoint;
import org.springframework.stereotype.Repository;

/**
 * @description 菜单与接口对应表
 * @table tb_menu_endpoint
 * @date 2019-12-31
 */
@Repository
public interface MenuEndpointMapper extends DataMapper<MenuEndpoint> {
}