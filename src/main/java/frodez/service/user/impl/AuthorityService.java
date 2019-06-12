package frodez.service.user.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import frodez.config.aop.exception.annotation.CatchAndReturn;
import frodez.config.aop.exception.annotation.CatchAndThrow;
import frodez.config.aop.validation.annotation.Check;
import frodez.config.security.auth.AuthorityManager;
import frodez.config.security.auth.AuthoritySource;
import frodez.config.security.util.Matcher;
import frodez.constant.enums.common.ModifyEnum;
import frodez.constant.enums.user.PermissionTypeEnum;
import frodez.constant.enums.user.UserStatusEnum;
import frodez.constant.errors.code.ErrorCode;
import frodez.constant.settings.PropertyKey;
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
import frodez.dao.param.user.UpdatePermission;
import frodez.dao.param.user.UpdateRole;
import frodez.dao.param.user.UpdateRolePermission;
import frodez.dao.result.user.PermissionDetail;
import frodez.dao.result.user.PermissionInfo;
import frodez.dao.result.user.RoleDetail;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.vm.facade.NameCache;
import frodez.service.cache.vm.facade.TokenCache;
import frodez.service.cache.vm.facade.UserIdCache;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.pair.Pair;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.common.EmptyUtil;
import frodez.util.common.StrUtil;
import frodez.util.reflect.BeanUtil;
import frodez.util.reflect.ReflectUtil;
import frodez.util.spring.MVCUtil;
import frodez.util.spring.PropertyUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

	@Autowired
	private AuthorityManager authorityManager;

	@Autowired
	private AuthoritySource authoritySource;

	@Check
	@CatchAndReturn
	@Override
	public Result getUserInfo(@NotNull Long userId) {
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
	}

	@Check
	@CatchAndReturn
	@Override
	public Result getUserInfo(@NotBlank String userName) {
		UserInfo data = nameCache.get(userName);
		if (data != null) {
			return Result.success(data);
		}
		Example example = new Example(User.class);
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
	}

	@Check
	@CatchAndReturn
	@Override
	public Result getUserInfos(@Valid @NotNull QueryPage param) {
		Page<User> page = PageHelper.startPage(QueryPage.safe(param)).doSelectPage(() -> {
			userMapper.selectAll();
		});
		return Result.page(page, getUserInfos(page.getResult()));
	}

	@Check
	@CatchAndReturn
	@Override
	public Result getUserInfosByIds(@NotEmpty List<Long> userIds, boolean includeFobiddens) {
		Example example = new Example(User.class);
		example.createCriteria().andIn("id", userIds);
		if (includeFobiddens) {
			example.and().andEqualTo("status", UserStatusEnum.NORMAL.getVal());
		}
		List<User> users = userMapper.selectByExample(example);
		if (users.size() != userIds.size()) {
			return Result.fail("存在非法的用户ID!");
		}
		return Result.success(getUserInfos(users));
	}

	@Check
	@CatchAndReturn
	@Override
	public Result getUserInfosByNames(@NotEmpty List<String> userNames, boolean includeFobiddens) {
		Example example = new Example(User.class);
		example.createCriteria().andIn("name", userNames);
		if (includeFobiddens) {
			example.and().andEqualTo("status", UserStatusEnum.NORMAL.getVal());
		}
		List<User> users = userMapper.selectByExample(example);
		if (users.size() != userNames.size()) {
			return Result.fail("存在非法的用户名!");
		}
		return Result.success(getUserInfos(users));
	}

	@Check
	@CatchAndReturn
	@Override
	public Result refreshUserInfoByIds(@NotEmpty List<Long> userIds, boolean includeFobiddens) {
		Example example = new Example(User.class);
		example.createCriteria().andIn("id", userIds);
		if (includeFobiddens) {
			example.and().andEqualTo("status", UserStatusEnum.NORMAL.getVal());
		}
		List<User> users = userMapper.selectByExample(example);
		if (users.size() != userIds.size()) {
			return Result.fail("存在非法的用户ID!");
		}
		refreshUserInfo(getUserInfos(users));
		return Result.success();
	}

	@Check
	@CatchAndReturn
	@Override
	public Result refreshUserInfoByNames(@NotEmpty List<String> userNames, boolean includeFobiddens) {
		Example example = new Example(User.class);
		example.createCriteria().andIn("name", userNames);
		if (includeFobiddens) {
			example.and().andEqualTo("status", UserStatusEnum.NORMAL.getVal());
		}
		List<User> users = userMapper.selectByExample(example);
		if (users.size() != userNames.size()) {
			return Result.fail("存在非法的用户名!");
		}
		refreshUserInfo(getUserInfos(users));
		return Result.success();
	}

	private List<UserInfo> getUserInfos(List<User> users) {
		List<Long> roleIds = users.stream().map(User::getRoleId).collect(Collectors.toList());
		Example example = new Example(Permission.class);
		example.createCriteria().andIn("id", rolePermissionMapper.batchGetPermissions(roleIds).stream().map(
			Pair::getValue).collect(Collectors.toList()));
		List<Permission> permissions = permissionMapper.selectByExample(example);
		example = new Example(Role.class);
		example.createCriteria().andIn("id", roleIds);
		Map<Long, Role> roleMap = roleMapper.selectByExample(example).stream().collect(Collectors.toMap(Role::getId, (
			iter) -> {
			return iter;
		}));
		Map<Long, List<PermissionInfo>> rolePermissionsMap = new HashMap<>();
		for (Long roleId : roleIds) {
			rolePermissionsMap.put(roleId, permissions.stream().filter((iter) -> {
				return roleId.equals(iter.getId());
			}).map((iter) -> {
				PermissionInfo info = new PermissionInfo();
				BeanUtil.copy(iter, info);
				return info;
			}).collect(Collectors.toList()));
		}
		List<UserInfo> userInfos = users.stream().map((user) -> {
			UserInfo info = new UserInfo();
			BeanUtil.copy(user, info);
			info.setRoleName(roleMap.get(user.getRoleId()).getName());
			info.setRoleLevel(roleMap.get(user.getRoleId()).getLevel());
			info.setRoleDescription(roleMap.get(user.getRoleId()).getDescription());
			info.setPermissionList(rolePermissionsMap.get(user.getRoleId()));
			return info;
		}).collect(Collectors.toList());
		return userInfos;
	}

	private void refreshUserInfo(List<UserInfo> userInfos) {
		Stream<UserInfo> stream = userInfos.stream();
		if (Runtime.getRuntime().availableProcessors() > 1 && userInfos.size() > 1024 || tokenCache.size() > 1024) {
			stream = stream.parallel();
		}
		stream.forEach((item) -> {
			userIdCache.save(item.getId(), item);
			nameCache.save(item.getName(), item);
			String token = tokenCache.getTokenByCondition((iter) -> {
				return iter.getId().equals(item.getId());
			});
			if (token != null) {
				tokenCache.save(token, item);
			}
		});
	}

	@Check
	@CatchAndReturn
	@Override
	public Result getPermission(@NotNull Long permissionId) {
		Permission permission = permissionMapper.selectByPrimaryKey(permissionId);
		if (permission == null) {
			return Result.fail("未找到该权限!");
		}
		PermissionDetail data = new PermissionDetail();
		BeanUtil.copy(permission, data);
		Example example = new Example(RolePermission.class);
		example.createCriteria().andEqualTo("permissionId", permissionId);
		data.setRoleIds(rolePermissionMapper.selectByExample(example).stream().map(RolePermission::getRoleId).collect(
			Collectors.toList()));
		return Result.success(data);
	}

	@Check
	@CatchAndReturn
	@Override
	public Result getPermissions(@Valid @NotNull QueryPage param) {
		return Result.page(PageHelper.startPage(QueryPage.safe(param)).doSelectPage(() -> permissionMapper
			.selectAll()));
	}

	@Check
	@CatchAndReturn
	@Override
	public Result getRole(@NotNull Long roleId) {
		Role role = roleMapper.selectByPrimaryKey(roleId);
		if (role == null) {
			return Result.fail("未找到该角色!");
		}
		RoleDetail data = new RoleDetail();
		BeanUtil.copy(role, data);
		Example example = new Example(RolePermission.class);
		example.createCriteria().andEqualTo("roleId", roleId);
		data.setPermissionIds(rolePermissionMapper.selectByExample(example).stream().map(
			RolePermission::getPermissionId).collect(Collectors.toList()));
		return Result.success(data);
	}

	@Check
	@CatchAndReturn
	@Override
	public Result getRoles(@Valid @NotNull QueryPage param) {
		return Result.page(PageHelper.startPage(QueryPage.safe(param)).doSelectPage(() -> roleMapper.selectAll()));
	}

	@Check
	@CatchAndReturn
	@Override
	public Result getRolePermissions(@Valid @NotNull QueryRolePermission param) {
		return Result.page(PageHelper.startPage(QueryPage.safe(param.getPage())).doSelectPage(() -> rolePermissionMapper
			.getPermissions(param.getRoleId())));
	}

	@Check
	@CatchAndThrow(errorCode = ErrorCode.AUTHORITY_SERVICE_ERROR)
	@Transactional
	@Override
	public Result addRole(@Valid @NotNull AddRole param) {
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
	}

	@Check
	@CatchAndThrow(errorCode = ErrorCode.AUTHORITY_SERVICE_ERROR)
	@Transactional
	@Override
	public Result updateRole(@Valid @NotNull UpdateRole param) {
		Role role = roleMapper.selectByPrimaryKey(param.getId());
		if (role == null) {
			return Result.fail("未找到该角色!");
		}
		if (param.getName() != null && checkRoleName(param.getName())) {
			return Result.fail("角色不能重名!");
		}
		roleMapper.updateByPrimaryKeySelective(BeanUtil.initialize(param, Role.class));
		return Result.success();
	}

	/**
	 * 检查角色名称是否重名,true为存在重名,false为不存在重名
	 * @author Frodez
	 * @date 2019-03-17
	 */
	private boolean checkRoleName(String name) {
		return roleMapper.selectAll().stream().filter((iter) -> {
			return iter.getName().equals(name);
		}).count() != 0;
	}

	/**
	 * 检查权限名称是否重名,true为存在重名,false为不存在重名
	 * @author Frodez
	 * @date 2019-03-17
	 */
	private boolean checkPermissionName(String name) {
		return permissionMapper.selectAll().stream().filter((iter) -> {
			return iter.getName().equals(name);
		}).count() != 0;
	}

	@Check
	@CatchAndThrow(errorCode = ErrorCode.AUTHORITY_SERVICE_ERROR)
	@Transactional
	@Override
	public Result addPermission(@Valid @NotNull AddPermission param) {
		try {
			if (checkPermissionName(param.getName())) {
				return Result.fail("权限不能重名!");
			}
			if (Matcher.isPermitAllPath(param.getUrl())) {
				return Result.fail("免验证路径不能配备权限!");
			}
			if (!checkPermissionUrl(PermissionTypeEnum.of(param.getType()), param.getUrl())) {
				return Result.fail("系统不存在与此匹配的url!");
			}
			Permission permission = new Permission();
			BeanUtil.copy(param, permission);
			permission.setCreateTime(new Date());
			permissionMapper.insert(permission);
			return Result.success();
		} finally {
			authorityManager.refresh();
			authoritySource.refresh();
		}
	}

	@Check
	@CatchAndThrow(errorCode = ErrorCode.AUTHORITY_SERVICE_ERROR)
	@Transactional
	@Override
	public Result updatePermission(@Valid @NotNull UpdatePermission param) {
		try {
			if (param.getType() == null && param.getUrl() != null || param.getType() != null && param
				.getUrl() == null) {
				return Result.errorRequest("类型和url必须同时存在!");
			}
			if (param.getUrl() != null && Matcher.isPermitAllPath(param.getUrl())) {
				return Result.fail("免验证路径不能配备权限!");
			}
			if (!checkPermissionUrl(PermissionTypeEnum.of(param.getType()), param.getUrl())) {
				return Result.fail("系统不存在与此匹配的url!");
			}
			Permission permission = permissionMapper.selectByPrimaryKey(param.getId());
			if (permission == null) {
				return Result.fail("找不到该权限!");
			}
			if (param.getName() != null && checkPermissionName(param.getName())) {
				return Result.fail("权限不能重名!");
			}
			permissionMapper.updateByPrimaryKeySelective(BeanUtil.initialize(param, Permission.class));
			return Result.success();
		} finally {
			authorityManager.refresh();
			authoritySource.refresh();
		}
	}

	/**
	 * 检查权限url是否符合要求,true为符合要求,false为不符合要求
	 * @author Frodez
	 * @date 2019-03-17
	 */
	private boolean checkPermissionUrl(PermissionTypeEnum type, String url) {
		switch (type) {
			case GET : {
				return MVCUtil.endPoints().get(RequestMethod.GET).stream().filter((iter) -> {
					return iter.getPatternsCondition().getPatterns().iterator().next().equals(url);
				}).count() != 0;
			}
			case POST : {
				return MVCUtil.endPoints().get(RequestMethod.POST).stream().filter((iter) -> {
					return iter.getPatternsCondition().getPatterns().iterator().next().equals(url);
				}).count() != 0;
			}
			case DELETE : {
				return MVCUtil.endPoints().get(RequestMethod.DELETE).stream().filter((iter) -> {
					return iter.getPatternsCondition().getPatterns().iterator().next().equals(url);
				}).count() != 0;
			}
			case PUT : {
				return MVCUtil.endPoints().get(RequestMethod.PUT).stream().filter((iter) -> {
					return iter.getPatternsCondition().getPatterns().iterator().next().equals(url);
				}).count() != 0;
			}
			case ALL : {
				return MVCUtil.endPoints().values().stream().flatMap(Collection::stream).filter((iter) -> {
					return iter.getPatternsCondition().getPatterns().contains(url);
				}).count() != 0;
			}
			default : {
				throw new RuntimeException("错误的类型!");
			}
		}
	}

	@Check
	@CatchAndThrow(errorCode = ErrorCode.AUTHORITY_SERVICE_ERROR)
	@Transactional
	@Override
	public Result updateRolePermission(@Valid @NotNull UpdateRolePermission param) {
		try {
			if (ModifyEnum.UPDATE.getVal() != param.getOperationType() && EmptyUtil.yes(param.getPermissionIds())) {
				return Result.errorRequest("不能对角色新增或者删除一个空的权限!");
			}
			Role role = roleMapper.selectByPrimaryKey(param.getRoleId());
			if (role == null) {
				return Result.fail("找不到该角色!");
			}
			switch (ModifyEnum.of(param.getOperationType())) {
				case INSERT : {
					Example example = new Example(Permission.class);
					example.createCriteria().andIn("id", param.getPermissionIds());
					List<Long> permissionIds = permissionMapper.selectByExample(example).stream().map(Permission::getId)
						.collect(Collectors.toList());
					if (permissionIds.size() != param.getPermissionIds().size()) {
						return Result.fail("存在错误的权限!");
					}
					example = new Example(RolePermission.class);
					example.createCriteria().andIn("permissionId", param.getPermissionIds()).andEqualTo("roleId", param
						.getRoleId());
					if (rolePermissionMapper.selectCountByExample(example) != 0) {
						return Result.fail("不能添加已拥有的权限!");
					}
					Date date = new Date();
					List<RolePermission> rolePermissions = param.getPermissionIds().stream().map((iter) -> {
						RolePermission item = new RolePermission();
						item.setCreateTime(date);
						item.setPermissionId(iter);
						item.setRoleId(param.getRoleId());
						return item;
					}).collect(Collectors.toList());
					rolePermissionMapper.insertList(rolePermissions);
					break;
				}
				case DELETE : {
					Example example = new Example(RolePermission.class);
					example.createCriteria().andIn("permissionId", param.getPermissionIds()).andEqualTo("roleId", param
						.getRoleId());
					if (rolePermissionMapper.selectCountByExample(example) != param.getPermissionIds().size()) {
						return Result.fail("存在错误的权限!");
					}
					rolePermissionMapper.deleteByExample(example);
					break;
				}
				case UPDATE : {
					if (EmptyUtil.no(param.getPermissionIds())) {
						Example example = new Example(Permission.class);
						example.createCriteria().andIn("id", param.getPermissionIds());
						if (permissionMapper.selectCountByExample(example) != param.getPermissionIds().size()) {
							return Result.fail("存在错误的权限!");
						}
					}
					Example example = new Example(RolePermission.class);
					example.createCriteria().andEqualTo("roleId", param.getRoleId());
					rolePermissionMapper.deleteByExample(example);
					if (EmptyUtil.no(param.getPermissionIds())) {
						Date date = new Date();
						List<RolePermission> rolePermissions = param.getPermissionIds().stream().map((iter) -> {
							RolePermission item = new RolePermission();
							item.setCreateTime(date);
							item.setPermissionId(iter);
							item.setRoleId(param.getRoleId());
							return item;
						}).collect(Collectors.toList());
						rolePermissionMapper.insertList(rolePermissions);
					}
					break;
				}
				default : {
					break;
				}
			}
			Example example = new Example(User.class);
			example.createCriteria().andEqualTo("roleId", param.getRoleId());
			refreshUserInfo(getUserInfos(userMapper.selectByExample(example)));
			return Result.success();
		} finally {
			authorityManager.refresh();
			authoritySource.refresh();
		}
	}

	@Check
	@CatchAndThrow(errorCode = ErrorCode.AUTHORITY_SERVICE_ERROR)
	@Transactional
	@Override
	public Result removeRole(@NotNull Long roleId) {
		try {
			Role role = roleMapper.selectByPrimaryKey(roleId);
			if (role == null) {
				return Result.fail("找不到该角色!");
			}
			Example example = new Example(User.class);
			example.createCriteria().andEqualTo("roleId", roleId);
			if (userMapper.selectCountByExample(example) != 0) {
				return Result.fail("仍存在使用该角色的用户,请更改该用户角色后再删除!");
			}
			roleMapper.deleteByPrimaryKey(roleId);
			example = new Example(RolePermission.class);
			example.createCriteria().andEqualTo("roleId", roleId);
			rolePermissionMapper.deleteByExample(example);
			return Result.success();
		} finally {
			authorityManager.refresh();
			authoritySource.refresh();
		}
	}

	@Check
	@CatchAndThrow(errorCode = ErrorCode.AUTHORITY_SERVICE_ERROR)
	@Transactional
	@Override
	public Result removePermission(@NotNull Long permissionId) {
		try {
			Permission permission = permissionMapper.selectByPrimaryKey(permissionId);
			if (permission == null) {
				return Result.fail("找不到该权限!");
			}
			Example example = new Example(RolePermission.class);
			example.createCriteria().andEqualTo("permissionId", permissionId);
			if (rolePermissionMapper.selectCountByExample(example) != 0) {
				return Result.fail("仍存在使用该权限的角色,请更改该角色权限后再删除!");
			}
			rolePermissionMapper.deleteByExample(example);
			permissionMapper.deleteByPrimaryKey(permissionId);
			return Result.success();
		} finally {
			authorityManager.refresh();
			authoritySource.refresh();
		}
	}

	@CatchAndThrow(errorCode = ErrorCode.AUTHORITY_SERVICE_ERROR)
	@Transactional
	@Override
	public Result scanAndCreatePermissions() {
		try {
			List<Permission> permissionList = new ArrayList<>();
			Date date = new Date();
			MVCUtil.requestMappingHandlerMappingStream().map((iter) -> {
				return iter.getHandlerMethods().entrySet();
			}).flatMap(Collection::stream).forEach((entry) -> {
				String requestUrl = StrUtil.concat(PropertyUtil.get(PropertyKey.Web.BASE_PATH), entry.getKey()
					.getPatternsCondition().getPatterns().stream().findFirst().get());
				if (!Matcher.needVerify(requestUrl)) {
					return;
				}
				requestUrl = requestUrl.substring(PropertyUtil.get(PropertyKey.Web.BASE_PATH).length());
				String requestType = entry.getKey().getMethodsCondition().getMethods().stream().map(RequestMethod::name)
					.findFirst().orElse(PermissionTypeEnum.ALL.name());
				String permissionName = ReflectUtil.getShortMethodName(entry.getValue().getMethod());
				Permission permission = new Permission();
				permission.setCreateTime(date);
				permission.setUrl(requestUrl);
				permission.setName(permissionName);
				permission.setDescription(permissionName);
				if (requestType.equals("GET")) {
					permission.setType(PermissionTypeEnum.GET.getVal());
				} else if (requestType.equals("POST")) {
					permission.setType(PermissionTypeEnum.POST.getVal());
				} else if (requestType.equals("DELETE")) {
					permission.setType(PermissionTypeEnum.DELETE.getVal());
				} else if (requestType.equals("PUT")) {
					permission.setType(PermissionTypeEnum.PUT.getVal());
				} else {
					permission.setType(PermissionTypeEnum.ALL.getVal());
				}
				permissionList.add(permission);
			});
			permissionMapper.insertList(permissionList);
			return Result.success();
		} finally {
			authorityManager.refresh();
			authoritySource.refresh();
		}
	}

}
