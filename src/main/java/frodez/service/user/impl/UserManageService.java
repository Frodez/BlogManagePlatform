package frodez.service.user.impl;

import frodez.config.security.util.UserUtil;
import frodez.constant.enums.user.UserStatus;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.result.permission.PermissionDetail;
import frodez.dao.model.result.user.UserBaseInfo;
import frodez.dao.model.result.user.UserDetail;
import frodez.dao.model.result.user.UserEndpointDetail;
import frodez.dao.model.result.user.UserInfo;
import frodez.dao.model.table.permission.Endpoint;
import frodez.dao.model.table.user.Role;
import frodez.dao.model.table.user.User;
import frodez.dao.param.user.UpdateUserRole;
import frodez.service.cache.facade.user.IdTokenCache;
import frodez.service.cache.facade.user.RoleCache;
import frodez.service.cache.facade.user.UserCache;
import frodez.service.permission.facade.IPermissionService;
import frodez.service.user.facade.IUserManageService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.common.EmptyUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserManageService implements IUserManageService {

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
	private UserMapper userMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private IPermissionService permissionService;

	@Override
	public Result getUsers(QueryPage query) {
		return query.start(() -> userMapper.pageBaseInfo(UserUtil.pass("includeForbidden")));
	}

	@Override
	public Result getUserInfo(Long userId) {
		UserBaseInfo user = userCache.get(userId);
		if (user == null) {
			user = userMapper.getBaseInfoById(userId, UserUtil.pass("includeForbidden"));
			if (user == null) {
				return Result.fail("未查询到用户信息!");
			}
		}
		if (UserUtil.reject("includeForbidden") && UserStatus.FORBIDDEN.getVal().equals(user.getStatus())) {
			return Result.fail("用户已禁用!");
		}
		Role role = roleCache.get(userId);
		if (role == null) {
			role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if (role == null) {
				return Result.fail("未查询到用户角色信息!");
			}
		}
		//必须一起存入缓存
		userCache.save(userId, user);
		roleCache.save(userId, role);
		UserInfo userInfo = new UserInfo();
		userInfo.setUser(user);
		userInfo.setRole(role);
		return Result.success(userInfo);
	}

	@Override
	public Result getUserInfo(String userName) {
		UserBaseInfo user = userMapper.getBaseInfoByName(userName, UserUtil.pass("includeForbidden"));
		if (user == null) {
			return Result.fail("未查询到用户信息!");
		}
		Long userId = user.getId();
		Role role = roleCache.get(userId);
		if (role == null) {
			role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if (role == null) {
				return Result.fail("未查询到用户角色信息!");
			}
		}
		//必须一起存入缓存
		userCache.save(userId, user);
		roleCache.save(userId, role);
		UserInfo userInfo = new UserInfo();
		userInfo.setUser(user);
		userInfo.setRole(role);
		return Result.success(userInfo);
	}

	@Override
	public Result getUserDetail(Long userId) {
		UserBaseInfo user = userMapper.getBaseInfoById(userId, UserUtil.pass("includeForbidden"));
		Long roleId = user.getRoleId();
		Role role = roleMapper.selectByPrimaryKey(roleId);
		if (role == null) {
			return Result.fail("未查询到用户角色信息!");
		}
		Result result = permissionService.getPermission(roleId);
		if (result.unable()) {
			return result;
		}
		PermissionDetail permission = result.as(PermissionDetail.class);
		//用户详情
		UserDetail detail = new UserDetail();
		detail.setUser(user);
		detail.setRole(role);
		detail.setPermission(permission);
		return Result.success(detail);
	}

	@Override
	public Result getUserDetail(String userName) {
		UserBaseInfo user = userMapper.getBaseInfoByName(userName, UserUtil.pass("includeForbidden"));
		Long roleId = user.getRoleId();
		Role role = roleMapper.selectByPrimaryKey(roleId);
		if (role == null) {
			return Result.fail("未查询到用户角色信息!");
		}
		Result result = permissionService.getPermission(roleId);
		if (result.unable()) {
			return result;
		}
		PermissionDetail permission = result.as(PermissionDetail.class);
		//用户详情
		UserDetail detail = new UserDetail();
		detail.setUser(user);
		detail.setRole(role);
		detail.setPermission(permission);
		return Result.success(detail);
	}

	@Override
	public Result getEndpointPermission(Long userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		return generateUserEndpointDetail(user);
	}

	@Override
	public Result getEndpointPermission(String userName) {
		User user = userMapper.selectOneEqual("name", userName);
		return generateUserEndpointDetail(user);
	}

	/**
	 * 获取用户后端端点权限的详细信息
	 * @author Frodez
	 * @date 2019-12-29
	 */
	private Result generateUserEndpointDetail(User user) {
		if (user == null) {
			return Result.fail("未查询到用户信息!");
		}
		if (UserUtil.reject("includeForbidden") && UserStatus.FORBIDDEN.getVal().equals(user.getStatus())) {
			return Result.fail("用户已禁用!");
		}
		Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
		if (role == null) {
			return Result.fail("未查询到用户角色信息!");
		}
		UserEndpointDetail detail = new UserEndpointDetail();
		Result result = permissionService.getEndpoints(role.getId());
		if (result.unable()) {
			return result;
		}
		detail.setUser(user);
		detail.setRole(role);
		detail.setEndpoints(result.list(Endpoint.class));
		return Result.success(detail);
	}

	@Override
	public Result setRole(UpdateUserRole param) {
		List<Long> userIds = param.getUserIds();
		if (userMapper.existByIds(userIds)) {
			return Result.fail("存在非法的用户ID!");
		}
		Long roleId = param.getRoleId();
		Role role = roleMapper.selectByPrimaryKey(roleId);
		if (role == null) {
			return Result.fail("未找到原角色!");
		}
		User record = new User();
		record.setRoleId(roleId);
		userMapper.updateIn("id", userIds, record);
		roleCache.save(userIds, role);
		idTokenCache.batchRemove(userIds, idTokenCache.getTokens(userIds));
		return Result.success();
	}

	@Override
	public Result replaceRole(Long former, Long latter) {
		if (!roleMapper.existsWithPrimaryKey(former)) {
			return Result.fail("未找到原角色!");
		}
		Role role = roleMapper.selectByPrimaryKey(latter);
		if (role == null) {
			return Result.fail("未找到新角色!");
		}
		List<User> users = userMapper.selectEqual("role_id", former);
		if (EmptyUtil.yes(users)) {
			return Result.success();
		}
		List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
		User record = new User();
		record.setRoleId(latter);
		kickAllOut(former);
		userMapper.updateIn("id", userIds, record);
		roleCache.save(userIds, role);
		return Result.success();
	}

	@Override
	public Result kickOut(Long userId) {
		String token = idTokenCache.getToken(userId);
		idTokenCache.remove(userId);
		if (token != null) {
			idTokenCache.remove(token);
		}
		return Result.success();
	}

	@Override
	public Result kickSomeOut(List<Long> userIds) {
		idTokenCache.batchRemove(userIds, idTokenCache.getTokens(userIds));
		return Result.success();
	}

	@Override
	public Result kickAllOut(Long roleId) {
		List<Long> ids = userMapper.partialEqual("id", "role_id", roleId);
		if (EmptyUtil.yes(ids)) {
			//如果该角色下无人,则直接返回成功
			return Result.success();
		}
		kickSomeOut(ids);
		return Result.success();
	}

	@Override
	public Result setStatus(Long userId, Byte status) {
		User user = new User();
		user.setStatus(status);
		userMapper.updateEqualSelective("id", userId, user);
		if (UserStatus.FORBIDDEN.getVal().equals(status)) {
			//还需要把用户踢出
			kickOut(userId);
		}
		return Result.success();
	}

	@Override
	public Result setStatus(List<Long> userIds, Byte status) {
		User user = new User();
		user.setStatus(status);
		userMapper.updateInSelective("id", userIds, user);
		if (UserStatus.FORBIDDEN.getVal().equals(status)) {
			//还需要把用户踢出
			kickSomeOut(userIds);
		}
		return Result.success();
	}

}
