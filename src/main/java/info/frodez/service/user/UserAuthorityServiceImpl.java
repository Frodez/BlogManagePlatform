package info.frodez.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import info.frodez.config.security.impl.util.JwtTokenUtil;
import info.frodez.constant.redis.RedisKey;
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
import info.frodez.util.result.ResultEnum;
import info.frodez.util.validation.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 用户授权服务
 * @author Frodez
 * @date 2018-11-14
 */
@Slf4j
@Service
public class UserAuthorityServiceImpl implements IUserAuthorityService {

	/**
	 * jwt工具类
	 */
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

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
			if(StringUtils.isBlank(userName)) {
				return new Result(ResultEnum.FAIL, "用户姓名不能为空!");
			}
			String json = redisService.getString(RedisKey.User.BASE_INFO + userName);
			if(StringUtils.isNotBlank(json)) {
				UserInfo data = JSONUtil.toObject(json, UserInfo.class);
				if(data != null) {
					return new Result(ResultEnum.SUCCESS, data);
				}
			}
			Example example = new Example(User.class);
			example.createCriteria().andEqualTo("name", userName);
			User user = userMapper.selectOneByExample(example);
			if(user == null) {
				return new Result(ResultEnum.FAIL, "未查询到用户信息!");
			}
			if(user.getStatus().equals(UserStatusEnum.FORBIDDEN.getValue())) {
				return new Result(ResultEnum.FAIL, "用户已禁用!");
			}
			Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if(role == null) {
				return new Result(ResultEnum.FAIL, "未查询到用户角色信息!");
			}
			List<PermissionInfo> permissionList = permissionMapper.getPermissions(user.getRoleId());
			UserInfo data = new UserInfo();
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
			redisService.set(RedisKey.User.BASE_INFO + userName, JSONUtil.toJSONString(data));
			return new Result(ResultEnum.SUCCESS, data);
		} catch (Exception e) {
			log.error("[getUserAuthority]", e);
			return new Result(ResultEnum.FAIL);
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
			String json = redisService.getString(RedisKey.User.PERMISSION_ALL);
			if(StringUtils.isNotBlank(json)) {
				List<Permission> permissions = JSONUtil.toList(json, Permission.class);
				if(permissions != null) {
					return new Result(ResultEnum.SUCCESS, permissions);
				}
			}
			List<Permission> permissions = permissionMapper.selectAll();
			redisService.set(RedisKey.User.PERMISSION_ALL, JSONUtil.toJSONString(permissions));
			return new Result(ResultEnum.SUCCESS, permissions);
		} catch (Exception e) {
			log.error("[getAllPermissions]", e);
			return new Result(ResultEnum.FAIL);
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
			String msg = ValidationUtil.validate(param);
			if(StringUtils.isNotBlank(msg)) {
				return new Result(ResultEnum.FAIL, msg);
			}
			Example example = new Example(User.class);
			example.createCriteria().andEqualTo("name", param.getUsername());
			User user = userMapper.selectOneByExample(example);
			if(user == null) {
				return new Result(ResultEnum.FAIL, "用户名或密码错误!");
			}
			if(user.getStatus().equals(UserStatusEnum.FORBIDDEN.getValue())) {
				return new Result(ResultEnum.FAIL, "用户已禁用!");
			}
			Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if(role == null) {
				return new Result(ResultEnum.FAIL, "未查询到用户角色信息!");
			}
			List<String> authorities = permissionMapper
					.getPermissions(role.getId()).stream()
					.map(PermissionInfo::getName).collect(Collectors.toList());
			String token = jwtTokenUtil.generate(param.getUsername(), authorities);
			return new Result(ResultEnum.SUCCESS, token);
		} catch (Exception e) {
			log.error("[login]", e);
			return new Result(ResultEnum.FAIL);
		}
	}

}
