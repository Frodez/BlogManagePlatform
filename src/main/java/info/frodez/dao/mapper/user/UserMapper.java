
package info.frodez.dao.mapper.user;

import info.frodez.config.mybatis.DataMapper;
import info.frodez.dao.model.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @description 用户表
 * @table tb_user
 * @date 2019-01-13
 */
@Mapper
@Repository
public interface UserMapper extends DataMapper<User> {
}