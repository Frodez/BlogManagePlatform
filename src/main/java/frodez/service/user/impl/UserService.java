package frodez.service.user.impl;

import frodez.config.error.exception.ServiceException;
import frodez.config.error.status.ErrorCode;
import frodez.config.security.util.TokenManager;
import frodez.constant.cache.UserKey;
import frodez.constant.user.UserStatusEnum;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.user.Role;
import frodez.dao.model.user.User;
import frodez.dao.param.user.LoginDTO;
import frodez.dao.param.user.RegisterDTO;
import frodez.dao.result.user.PermissionInfo;
import frodez.service.cache.RedisService;
import frodez.service.user.facade.IUserService;
import frodez.util.result.Result;
import frodez.util.result.ResultUtil;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * 用户信息服务
 * @author Frodez
 * @date 2018-11-14
 */
@Slf4j
@Service
public class UserService implements IUserService {

	/**
	 * jwt工具类
	 */
	@Autowired
	private TokenManager tokenUtil;

	/**
	 * spring security验证管理器
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * redis服务
	 */
	@Autowired
	private RedisService redisService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public Result login(LoginDTO param) {
		try {
			Example example = new Example(User.class);
			example.createCriteria().andEqualTo("name", param.getUsername());
			User user = userMapper.selectOneByExample(example);
			if (user == null) {
				return ResultUtil.fail("用户名或密码错误!");
			}
			if (user.getStatus().equals(UserStatusEnum.FORBIDDEN.getVal())) {
				return ResultUtil.fail("用户已禁用!");
			}
			Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if (role == null) {
				return ResultUtil.fail("未查询到用户角色信息!");
			}
			List<String> authorities = permissionMapper.getPermissions(role.getId()).stream().map(
				PermissionInfo::getName).collect(Collectors.toList());
			String token = tokenUtil.generate(param.getUsername(), authorities);
			// 这里将token作为key,userName作为value存入redis,方便之后通过token获取用户信息
			redisService.set(UserKey.TOKEN + token, user.getName());
			SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword())));
			return ResultUtil.success(token);
		} catch (Exception e) {
			log.error("[login]", e);
			return ResultUtil.errorService();
		}
	}

	@Override
	@Transactional
	public Result register(RegisterDTO param) {
		try {
			User user = new User();
			Date date = new Date();
			user.setCreateTime(date);
			user.setName(param.getName());
			String password = passwordEncoder.encode(param.getPassword());
			user.setPassword(password);
			user.setNickname(param.getNickname());
			user.setEmail(param.getEmail());
			user.setPhone(param.getPhone());
			user.setStatus(UserStatusEnum.NORMAL.getVal());
			user.setRoleId(1L);
			userMapper.insert(user);
			return ResultUtil.success();
		} catch (Exception e) {
			log.error("[register]", e);
			throw new ServiceException(ErrorCode.USER_SERVICE_ERROR);
		}
	}

}
