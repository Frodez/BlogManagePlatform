package frodez.service.user.impl;

import frodez.config.aop.exception.annotation.Error;
import frodez.config.security.util.UserUtil;
import frodez.constant.enums.user.UserStatus;
import frodez.constant.errors.code.ErrorCode;
import frodez.constant.keys.config.IntKey;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.table.user.User;
import frodez.dao.param.user.RegisterUser;
import frodez.dao.param.user.UpdateUser;
import frodez.service.cache.facade.config.IGlobalDataCache;
import frodez.service.cache.facade.user.IdTokenCache;
import frodez.service.cache.facade.user.RoleCache;
import frodez.service.cache.facade.user.UserCache;
import frodez.service.user.facade.ILoginService;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.result.Result;
import frodez.util.reflect.BeanUtil;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Error(ErrorCode.USER_SERVICE_ERROR)
public class UserService implements IUserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ILoginService loginService;

	@Autowired
	@Qualifier("idTokenRedisCache")
	private IdTokenCache idTokenCache;

	@Autowired
	@Qualifier("userMapCache")
	private UserCache userCache;

	@Autowired
	@Qualifier("roleMapCache")
	private RoleCache roleCache;

	@Autowired
	@Qualifier("globalDataMapCache")
	private IGlobalDataCache globalDataCache;

	@Autowired
	private UserMapper userMapper;

	@Override
	public Result register(RegisterUser param) {
		User user = new User();
		user.setCreateTime(new Date());
		user.setName(param.getName());
		user.setPassword(passwordEncoder.encode(param.getPassword()));
		user.setNickname(param.getNickname());
		user.setEmail(param.getEmail());
		user.setPhone(param.getPhone());
		user.setStatus(UserStatus.NORMAL.getVal());
		//暂时写死
		user.setRoleId(globalDataCache.get(IntKey.DEFAULT_USER_ROLE));
		userMapper.insert(user);
		return Result.success();
	}

	/**
	 * 用户注销
	 * @author Frodez
	 * @date 2019-03-15
	 */
	@Override
	public Result logOff() {
		String token = UserUtil.token();
		Long id = idTokenCache.getId(token);
		if (id == null) {
			//如果cache里找不到token对应的id,属于异常情况
			return Result.fail("需要登录后才能注销!");
		}
		userMapper.deleteByPrimaryKey(id);
		loginService.logout().orThrowMessage();
		//必须在事务成功后删除缓存
		idTokenCache.remove(token);
		return Result.success();
	}

	@Override
	public Result setStatus(Long userId, Byte status) {
		User user = new User();
		user.setStatus(status);
		userMapper.updateEqualSelective("id", userId, user);
		return Result.success();
	}

	@Override
	public Result setStatus(List<Long> userIds, Byte status) {
		User user = new User();
		user.setStatus(status);
		userMapper.updateInSelective("id", userIds, user);
		return Result.success();
	}

	@Override
	public Result updateUser(UpdateUser param) {
		userMapper.updateByPrimaryKeySelective(BeanUtil.copy(param, User::new));
		return Result.success();
	}

	@Override
	public Result updatePassword(String password) {
		String userName = UserUtil.name();
		if (userName == null) {
			return Result.fail();
		}
		User record = new User();
		record.setPassword(passwordEncoder.encode(password));
		userMapper.updateEqualSelective("name", userName, record);
		return Result.success();
	}

}
