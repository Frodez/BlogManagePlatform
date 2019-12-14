package frodez.service.user.impl;

import frodez.config.aop.exception.annotation.Error;
import frodez.config.security.util.UserUtil;
import frodez.constant.enums.user.UserStatusEnum;
import frodez.constant.errors.code.ErrorCode;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.user.User;
import frodez.dao.param.user.Doregister;
import frodez.service.user.facade.ILoginService;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.result.Result;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户信息服务
 * @author Frodez
 * @date 2018-11-14
 */
@Service
@Error(ErrorCode.USER_SERVICE_ERROR)
public class UserService implements IUserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ILoginService loginService;

	@Transactional
	@Override
	public Result register(Doregister param) {
		User user = new User();
		user.setCreateTime(new Date());
		user.setName(param.getName());
		user.setPassword(passwordEncoder.encode(param.getPassword()));
		user.setNickname(param.getNickname());
		user.setEmail(param.getEmail());
		user.setPhone(param.getPhone());
		user.setStatus(UserStatusEnum.NORMAL.getVal());
		//暂时写死
		user.setRoleId(1L);
		userMapper.insert(user);
		return Result.success();
	}

	/**
	 * 用户注销
	 * @author Frodez
	 * @date 2019-03-15
	 */
	@Transactional
	@Override
	public Result logOff() {
		userMapper.deleteByPrimaryKey(UserUtil.get().getId());
		loginService.logout().ableOrThrow();
		return Result.success();
	}

}
