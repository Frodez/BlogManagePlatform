package info.frodez.service.user;

import info.frodez.config.security.util.TokenUtil;
import info.frodez.constant.redis.Redis;
import info.frodez.constant.user.UserStatusEnum;
import info.frodez.dao.mapper.user.PermissionMapper;
import info.frodez.dao.mapper.user.RoleMapper;
import info.frodez.dao.mapper.user.UserMapper;
import info.frodez.dao.model.user.Permission;
import info.frodez.dao.model.user.Role;
import info.frodez.dao.model.user.User;
import info.frodez.dao.param.user.LoginDTO;
import info.frodez.dao.result.user.PermissionInfo;
import info.frodez.dao.result.user.UserInfo;
import info.frodez.service.redis.RedisService;
import info.frodez.util.json.JSONUtil;
import info.frodez.util.result.Result;
import info.frodez.util.result.ResultUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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
	private TokenUtil jwtTokenUtil;

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
	@Override
	public Result getUserInfoByName(String userName) {
		try {
			String json = redisService.getString(Redis.User.BASE_INFO + userName);
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
			redisService.set(Redis.User.BASE_INFO + userName, JSONUtil.toJSONString(data));
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
			String json = redisService.getString(Redis.User.PERMISSION_ALL);
			List<Permission> permissions = JSONUtil.toList(json, Permission.class);
			if (permissions != null) {
				return ResultUtil.success(permissions);
			}
			permissions = permissionMapper.selectAll();
			redisService.set(Redis.User.PERMISSION_ALL, JSONUtil.toJSONString(permissions));
			return ResultUtil.success(permissions);
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
			String token = jwtTokenUtil.generate(param.getUsername(), authorities);
			return ResultUtil.success(token);
		} catch (Exception e) {
			log.error("[login]", e);
			return ResultUtil.fail();
		}
	}

}
