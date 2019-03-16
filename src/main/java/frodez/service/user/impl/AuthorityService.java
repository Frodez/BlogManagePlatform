package frodez.service.user.impl;

import com.github.pagehelper.PageHelper;
import frodez.config.aop.validation.annotation.Check;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.mapper.user.RolePermissionMapper;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.user.Permission;
import frodez.dao.model.user.Role;
import frodez.dao.model.user.RolePermission;
import frodez.dao.model.user.User;
import frodez.dao.param.user.AddPermission;
import frodez.dao.param.user.AddRole;
import frodez.dao.param.user.QueryRolePermission;
import frodez.dao.param.user.SetRolePermission;
import frodez.dao.result.user.PermissionInfo;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.vm.facade.NameCache;
import frodez.service.cache.vm.facade.TokenCache;
import frodez.service.cache.vm.facade.UserIdCache;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.pair.Pair;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.common.EmptyUtil;
import frodez.util.constant.user.PermissionTypeEnum;
import frodez.util.constant.user.UserStatusEnum;
import frodez.util.error.ErrorCode;
import frodez.util.error.exception.ServiceException;
import frodez.util.mapper.ExampleUtil;
import frodez.util.reflect.BeanUtil;
import frodez.util.spring.context.ContextUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import tk.mybatis.mapper.entity.Example;

/**
 * 权限信息服务
 * @author Frodez
 * @date 2018-11-14
 */
@Slf4j
@Service
public class AuthorityService implements IAuthorityService {

	@Autowired
	private UserIdCache userIdCache;

	@Autowired
	private TokenCache tokenCache;

	@Autowired
	private NameCache nameCache;

	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public Result getUserInfo(Long userId) {
		try {
			UserInfo data = userIdCache.get(userId);
			if (data != null) {
				return Result.success(data);
			}
			User user = userMapper.selectByPrimaryKey(userId);
			if (user == null) {
				return Result.fail("未查询到用户信息!");
			}
			if (user.getStatus().equals(UserStatusEnum.FORBIDDEN.getVal())) {
				return Result.fail("用户已禁用!");
			}
			Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if (role == null) {
				return Result.fail("未查询到用户角色信息!");
			}
			List<PermissionInfo> permissionList = rolePermissionMapper.getPermissions(user.getRoleId());
			data = new UserInfo();
			BeanUtil.copy(user, data);
			data.setRoleName(role.getName());
			data.setRoleLevel(role.getLevel());
			data.setRoleDescription(role.getDescription());
			data.setPermissionList(permissionList);
			userIdCache.save(userId, data);
			return Result.success(data);
		} catch (Exception e) {
			log.error("[getUserInfo]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result getUserInfo(String userName) {
		try {
			UserInfo data = nameCache.get(userName);
			if (data != null) {
				return Result.success(data);
			}
			Example example = ExampleUtil.get(User.class);
			example.createCriteria().andEqualTo("name", userName);
			User user = userMapper.selectOneByExample(example);
			if (user == null) {
				return Result.fail("未查询到用户信息!");
			}
			if (user.getStatus().equals(UserStatusEnum.FORBIDDEN.getVal())) {
				return Result.fail("用户已禁用!");
			}
			Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if (role == null) {
				return Result.fail("未查询到用户角色信息!");
			}
			List<PermissionInfo> permissionList = rolePermissionMapper.getPermissions(user.getRoleId());
			data = new UserInfo();
			BeanUtil.copy(user, data);
			data.setRoleName(role.getName());
			data.setRoleLevel(role.getLevel());
			data.setRoleDescription(role.getDescription());
			data.setPermissionList(permissionList);
			nameCache.save(userName, data);
			return Result.success(data);
		} catch (Exception e) {
			log.error("[getUserInfo]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result getUserInfosByIds(List<Long> userIds) {
		try {
			Example example = ExampleUtil.get(User.class);
			example.createCriteria().andIn("id", userIds);
			List<User> users = userMapper.selectByExample(example);
			if (users.size() != userIds.size()) {
				return Result.fail("存在错误的用户ID!");
			}
			return Result.success();
		} catch (Exception e) {
			log.error("[getUserInfosByIds]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result getUserInfosByNames(List<String> userNames) {
		try {
			Example example = new Example(User.class);
			example.createCriteria().andIn("name", userNames);
			List<User> users = userMapper.selectByExample(example);
			if (users.size() != userNames.size()) {
				return Result.fail("存在错误的用户ID!");
			}
			return Result.success(getUserInfos(users));
		} catch (Exception e) {
			log.error("[getUserInfosByNames]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result refreshUserInfoByIds(List<Long> userIds) {
		try {
			Example example = ExampleUtil.get(User.class);
			example.createCriteria().andIn("id", userIds);
			List<User> users = userMapper.selectByExample(example);
			if (users.size() != userIds.size()) {
				return Result.fail("存在错误的用户ID!");
			}
			refreshUserInfo(getUserInfos(users));
			return Result.success();
		} catch (Exception e) {
			log.error("[refreshUserInfoByIds]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result refreshUserInfoByNames(List<String> userNames) {
		try {
			Example example = new Example(User.class);
			example.createCriteria().andIn("name", userNames);
			List<User> users = userMapper.selectByExample(example);
			if (users.size() != userNames.size()) {
				return Result.fail("存在错误的用户ID!");
			}
			refreshUserInfo(getUserInfos(users));
			return Result.success();
		} catch (Exception e) {
			log.error("[refreshUserInfoByNames]", e);
			return Result.errorService();
		}
	}

	private List<UserInfo> getUserInfos(List<User> users) {
		List<Long> roleIds = users.stream().map(User::getRoleId).collect(Collectors.toList());
		Example example = ExampleUtil.get(Permission.class);
		example.createCriteria().andIn("id", rolePermissionMapper.batchGetPermissions(roleIds).stream().map(
			Pair::getValue).collect(Collectors.toList()));
		List<Permission> permissions = permissionMapper.selectByExample(example);
		example = ExampleUtil.get(Role.class);
		example.createCriteria().andIn("id", roleIds);
		Map<Long, Role> roleMap = roleMapper.selectByExample(example).stream().collect(Collectors.toMap(Role::getId, (
			iter) -> {
			return iter;
		}));
		Map<Long, List<PermissionInfo>> rolePermissionsMap = new HashMap<>();
		for (Long roleId : roleIds) {
			List<PermissionInfo> list = permissions.stream().filter((iter) -> {
				return roleId.equals(iter.getId());
			}).map((iter) -> {
				PermissionInfo info = new PermissionInfo();
				BeanUtil.copy(iter, info);
				return info;
			}).collect(Collectors.toList());
			rolePermissionsMap.put(roleId, list);
		}
		List<UserInfo> userInfos = new ArrayList<>();
		for (User user : users) {
			UserInfo info = new UserInfo();
			BeanUtil.copy(user, info);
			info.setRoleName(roleMap.get(user.getRoleId()).getName());
			info.setRoleLevel(roleMap.get(user.getRoleId()).getLevel());
			info.setRoleDescription(roleMap.get(user.getRoleId()).getDescription());
			info.setPermissionList(rolePermissionsMap.get(user.getRoleId()));
			userInfos.add(info);
		}
		return userInfos;
	}

	private void refreshUserInfo(List<UserInfo> userInfos) {
		Stream<UserInfo> stream = userInfos.stream();
		if (userInfos.size() > 1024 || tokenCache.size() > 1024) {
			stream = stream.parallel();
		}
		stream.forEach((item) -> {
			userIdCache.save(item.getId(), item);
			nameCache.save(item.getName(), item);
			tokenCache.save(tokenCache.getTokenByCondition((iter) -> {
				return iter.getId().equals(item.getId());
			}), item);
		});
	}

	@Check
	@Override
	public Result getPermissions(QueryPage param) {
		try {
			return Result.page(PageHelper.startPage(QueryPage.resonable(param)).doSelectPage(() -> permissionMapper
				.selectAll()));
		} catch (Exception e) {
			log.error("[getAllRoles]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result getRoles(QueryPage param) {
		try {
			return Result.page(PageHelper.startPage(QueryPage.resonable(param)).doSelectPage(() -> roleMapper
				.selectAll()));
		} catch (Exception e) {
			log.error("[getAllRoles]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result getRolePermissions(QueryRolePermission param) {
		try {
			return Result.page(PageHelper.startPage(QueryPage.resonable(param.getPage())).doSelectPage(
				() -> rolePermissionMapper.getPermissions(param.getRoleId())));
		} catch (Exception e) {
			log.error("[getAllPermissions]", e);
			return Result.errorService();
		}
	}

	@Check
	@Transactional
	@Override
	public Result addRole(AddRole param) {
		try {
			if (roleMapper.selectAll().stream().filter((iter) -> {
				return iter.getName().equals(param.getName());
			}).count() != 0) {
				return Result.fail("角色不能重名!");
			}
			Role role = new Role();
			BeanUtil.copy(param, role);
			role.setCreateTime(new Date());
			roleMapper.insertUseGeneratedKeys(role);
			if (EmptyUtil.no(param.getPermissionIds())) {
				Date date = new Date();
				List<RolePermission> rolePermissions = param.getPermissionIds().stream().map((id) -> {
					RolePermission item = new RolePermission();
					item.setCreateTime(date);
					item.setRoleId(role.getId());
					item.setPermissionId(id);
					return item;
				}).collect(Collectors.toList());
				rolePermissionMapper.insertList(rolePermissions);
			}
			return Result.success();
		} catch (Exception e) {
			log.error("[addRole]", e);
			throw new ServiceException(ErrorCode.USER_SERVICE_ERROR);
		}
	}

	@Check
	@Transactional
	@Override
	public Result addPermission(AddPermission param) {
		try {
			if (permissionMapper.selectAll().stream().filter((iter) -> {
				return iter.getName().equals(param.getName());
			}).count() != 0) {
				return Result.fail("权限不能重名!");
			}
			switch (PermissionTypeEnum.of(param.getType())) {
				case GET : {
					if (ContextUtil.getAllEndPoints().get(RequestMethod.GET).stream().filter((iter) -> {
						return iter.getPatternsCondition().getPatterns().iterator().next().equals(param.getUrl());
					}).count() == 0) {
						return Result.fail("系统不存在与此匹配的url!");
					}
					break;
				}
				case POST : {
					if (ContextUtil.getAllEndPoints().get(RequestMethod.POST).stream().filter((iter) -> {
						return iter.getPatternsCondition().getPatterns().iterator().next().equals(param.getUrl());
					}).count() == 0) {
						return Result.fail("系统不存在与此匹配的url!");
					}
					break;
				}
				case DELETE : {
					if (ContextUtil.getAllEndPoints().get(RequestMethod.DELETE).stream().filter((iter) -> {
						return iter.getPatternsCondition().getPatterns().iterator().next().equals(param.getUrl());
					}).count() == 0) {
						return Result.fail("系统不存在与此匹配的url!");
					}
					break;
				}
				case PUT : {
					if (ContextUtil.getAllEndPoints().get(RequestMethod.PUT).stream().filter((iter) -> {
						return iter.getPatternsCondition().getPatterns().iterator().next().equals(param.getUrl());
					}).count() == 0) {
						return Result.fail("系统不存在与此匹配的url!");
					}
					break;
				}
				case ALL : {
					if (ContextUtil.getAllEndPoints().get(RequestMethod.GET).stream().filter((iter) -> {
						return iter.getPatternsCondition().getPatterns().iterator().next().equals(param.getUrl());
					}).count() == 0) {
						return Result.fail("系统不存在与此匹配的url!");
					}
					break;
				}
				default : {
					if (ContextUtil.getAllEndPoints().values().stream().flatMap(Collection::stream).filter((iter) -> {
						return iter.getPatternsCondition().getPatterns().iterator().next().equals(param.getUrl());
					}).count() == 0) {
						return Result.fail("系统不存在与此匹配的url!");
					}
					break;
				}
			}
			Permission permission = new Permission();
			BeanUtil.copy(param, permission);
			permission.setCreateTime(new Date());
			permissionMapper.insert(permission);
			return Result.success();
		} catch (Exception e) {
			log.error("[addPermission]", e);
			throw new ServiceException(ErrorCode.USER_SERVICE_ERROR);
		}
	}

	@Check
	@Transactional
	@Override
	public Result setRolePermission(SetRolePermission param) {
		try {
			return Result.success();
		} catch (Exception e) {
			log.error("[setRolePermission]", e);
			throw new ServiceException(ErrorCode.USER_SERVICE_ERROR);
		}
	}

}
