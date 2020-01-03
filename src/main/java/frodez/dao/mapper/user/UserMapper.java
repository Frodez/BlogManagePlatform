package frodez.dao.mapper.user;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.constant.annotations.decoration.Page;
import frodez.dao.model.result.user.UserBaseInfo;
import frodez.dao.model.table.user.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description 用户表
 * @table tb_user
 * @date 2019-03-06
 */
@Repository
public interface UserMapper extends DataMapper<User> {

	/**
	 * 根据用户id查询UserBaseInfo
	 * @author Frodez
	 * @date 2019-12-31
	 */
	UserBaseInfo getBaseInfoById(@Param("userId") Long userId, @Param("includeForbidden") boolean includeForbidden);

	/**
	 * 根据用户id查询UserBaseInfo
	 * @author Frodez
	 * @date 2019-12-31
	 */
	UserBaseInfo getBaseInfoByName(@Param("userName") String userName, boolean includeForbidden);

	/**
	 * 根据用户id批量查询UserBaseInfo
	 * @author Frodez
	 * @date 2019-12-31
	 */
	List<UserBaseInfo> batchGetBaseInfoById(@Param("userIds") List<Long> userIds, @Param("includeForbidden") boolean includeForbidden);

	/**
	 * 分页查询UserBaseInfo
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Page
	List<UserBaseInfo> pageBaseInfo(@Param("includeForbidden") boolean includeForbidden);

}
