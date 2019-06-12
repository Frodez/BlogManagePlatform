package frodez.service.user.impl;

import frodez.config.aop.exception.annotation.CatchAndThrow;
import frodez.config.aop.validation.annotation.Check;
import frodez.config.security.util.UserUtil;
import frodez.constant.enums.user.UserStatusEnum;
import frodez.constant.errors.code.ErrorCode;
import frodez.constant.errors.code.ServiceException;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.user.User;
import frodez.dao.param.user.Doregister;
import frodez.dao.result.user.UserInfo;
import frodez.service.user.facade.ILoginService;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.result.Result;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
public class UserService implements IUserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ILoginService loginService;

	@Check
	@CatchAndThrow(errorCode = ErrorCode.USER_SERVICE_ERROR)
	@Transactional
	@Override
	public Result register(@Valid @NotNull Doregister param) {
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
	@CatchAndThrow(errorCode = ErrorCode.USER_SERVICE_ERROR)
	@Transactional
	@Override
	public Result logOff() {
		UserInfo userInfo = UserUtil.get();
		userMapper.deleteByPrimaryKey(userInfo.getId());
		Result result = loginService.logout();
		if (result.unable()) {
			throw new ServiceException(result);
		}
		return Result.success();
	}

}
