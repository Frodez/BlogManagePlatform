package info.frodez.dao.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import info.frodez.config.mybatis.DataMapper;
import info.frodez.dao.model.user.User;

/**
 * @description 用户表
 * @table tb_user
 * @date 2018-11-26
 */
@Repository
@Mapper
public interface UserMapper extends DataMapper<User> {
	
}