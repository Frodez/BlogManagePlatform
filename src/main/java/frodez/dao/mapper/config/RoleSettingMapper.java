package frodez.dao.mapper.config;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.dao.model.table.config.RoleSetting;
import org.springframework.stereotype.Repository;

/**
 * @description 角色设置表
 * @table tb_role_setting
 * @date 2020-01-01
 */
@Repository
public interface RoleSettingMapper extends DataMapper<RoleSetting> {
}