package frodez.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.security.util.TokenUtil;
import frodez.constant.cache.UserKey;
import frodez.constant.user.UserStatusEnum;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.user.Role;
import frodez.dao.model.user.User;
import frodez.dao.param.user.LoginDTO;
import frodez.dao.result.user.PermissionInfo;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.RedisService;
import frodez.util.json.JSONUtil;
import frodez.util.result.Result;
import frodez.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 用户信息服务
 * @author Frodez
 * @date 2018-11-14
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

	/**
	 * jwt工具类
	 */
	@Autowired
	private TokenUtil tokenUtil;

	/**
	 * spring security验证管理器
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

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

	/**
	 * 获取用户授权信息
	 * @author Frodez
	 * @param userName 用户姓名(唯一)
	 * @date 2018-11-14
	 */
	@Check
	@Override
	public Result getUserInfoByName(String userName) {
		try {
			String json = redisService.getString(UserKey.BASE_INFO + userName);
			UserInfo data = JSONUtil.toObject(json, UserInfo.class);
			if (data != null) {
				return ResultUtil.success(data);
			}
			Example example = new Example(User.class);
			example.createCriteria().andEqualTo("name", userName);
			User user = userMapper.selectOneByExample(example);
			if (user == null) {
				return ResultUtil.fail("未查询到用户信息!");
			}
			if (user.getStatus().equals(UserStatusEnum.FORBIDDEN.getValue())) {
				return ResultUtil.fail("用户已禁用!");
			}
			Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if (role == null) {
				return ResultUtil.fail("未查询到用户角色信息!");
			}
			List<PermissionInfo> permissionList = permissionMapper.getPermissions(user.getRoleId());
			data = new UserInfo();
			data.setId(user.getId());
			data.setPassword(user.getPassword());
			data.setName(userName);
			data.setNickname(user.getNickname());
			data.setEmail(user.getEmail());
			data.setPhone(user.getPhone());
			data.setRoleId(user.getRoleId());
			data.setRoleName(role.getName());
			data.setRoleLevel(role.getLevel());
			data.setRoleDescription(role.getDescription());
			data.setPermissionList(permissionList);
			redisService.set(UserKey.BASE_INFO + userName, JSONUtil.toJSONString(data));
			return ResultUtil.success(data);
		} catch (Exception e) {
			log.error("[getUserAuthority]", e);
			return ResultUtil.fail();
		}
	}

	/**
	 * 获取所有权限信息
	 * @author Frodez
	 * @date 2018-12-04
	 */
	@Override
	public Result getAllPermissions() {
		try {
			return ResultUtil.success(permissionMapper.selectAll());
		} catch (Exception e) {
			log.error("[getAllPermissions]", e);
			return ResultUtil.fail();
		}
	}

	/**
	 * 用户登录
	 * @author Frodez
	 * @param LoginDTO 用户登录请求参数
	 * @date 2018-12-03
	 */
	@Check
	@Override
	public Result login(LoginDTO param) {
		try {
			SecurityContextHolder.getContext().setAuthentication(authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword())));
			Example example = new Example(User.class);
			example.createCriteria().andEqualTo("name", param.getUsername());
			User user = userMapper.selectOneByExample(example);
			if (user == null) {
				return ResultUtil.fail("用户名或密码错误!");
			}
			if (user.getStatus().equals(UserStatusEnum.FORBIDDEN.getValue())) {
				return ResultUtil.fail("用户已禁用!");
			}
			Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if (role == null) {
				return ResultUtil.fail("未查询到用户角色信息!");
			}
			List<String> authorities = permissionMapper.getPermissions(role.getId()).stream()
				.map(PermissionInfo::getName).collect(Collectors.toList());
			String token = tokenUtil.generate(param.getUsername(), authorities);
			// 这里将token作为key,userName作为value存入redis,方便之后通过token获取用户信息
			redisService.set(UserKey.TOKEN + token, user.getName());
			return ResultUtil.success(token);
		} catch (Exception e) {
			log.error("[login]", e);
			return ResultUtil.fail();
		}
	}

}
