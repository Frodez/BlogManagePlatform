package frodez.service.user.mapper;

import frodez.config.mybatis.DataMapper;
import frodez.service.user.model.User;
import org.springframework.stereotype.Repository;

/**
 * @description 用户表
 * @table tb_user
 * @date 2019-01-13
 */
@Repository
public interface UserMapper extends DataMapper<User> {
}
