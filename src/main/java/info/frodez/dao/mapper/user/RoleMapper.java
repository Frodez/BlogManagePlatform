package info.frodez.dao.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import info.frodez.config.mybatis.DataMapper;
import info.frodez.dao.model.user.Role;

/**
 * @description 用户角色表
 * @table tb_role
 * @date 2018-11-14
 */
@Repository
@Mapper
public interface RoleMapper extends DataMapper<Role> {

}