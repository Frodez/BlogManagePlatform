package frodez.service.user.impl;

import frodez.config.aop.exception.annotation.Error;
import frodez.config.security.util.UserUtil;
import frodez.constant.enums.user.UserStatus;
import frodez.constant.errors.code.ErrorCode;
import frodez.constant.keys.config.IntKey;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.table.user.User;
import frodez.dao.param.user.RegisterUser;
import frodez.dao.param.user.UpdatePassword;
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
	public Result logOff(String name, String password) {
		User user = userMapper.selectOneEqual("name", name);
		if (user == null) {
			return Result.fail("该用户不存在");
		}
		if (passwordEncoder.matches(password, user.getPassword())) {
			return Result.fail("用户名或密码错误");
		}
		Long userId = user.getId();
		userMapper.deleteByPrimaryKey(userId);
		if (idTokenCache.exist(userId)) {
			loginService.logout().orThrowMessage();
		}
		userCache.remove(userId);
		return Result.success();
	}

	@Override
	public Result updateUser(UpdateUser param) {
		userMapper.updateByPrimaryKeySelective(BeanUtil.copy(param, User::new));
		return Result.success();
	}

	@Override
	public Result updatePassword(UpdatePassword param) {
		String userName = UserUtil.name();
		if (userName == null) {
			return Result.fail();
		}
		User user = userMapper.selectOneEqual("name", userName);
		if (!passwordEncoder.matches(param.getOldPassword(), user.getPassword())) {
			return Result.fail("原密码错误");
		}
		User record = new User();
		record.setPassword(passwordEncoder.encode(param.getNewPassword()));
		userMapper.updateEqualSelective("name", userName, record);
		return Result.success();
	}

}
