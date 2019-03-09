package frodez.service.user.impl;

import com.github.pagehelper.PageHelper;
import frodez.config.aop.validation.annotation.common.Check;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.user.Role;
import frodez.dao.model.user.User;
import frodez.dao.param.user.RolePermissionQuery;
import frodez.dao.result.user.PermissionInfo;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.vm.facade.UserIdCache;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.param.PageQuery;
import frodez.util.beans.result.Result;
import frodez.util.constant.user.UserStatusEnum;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户信息服务
 * @author Frodez
 * @date 2018-11-14
 */
@Slf4j
@Service
public class UserService implements IUserService {

	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserIdCache userIdCache;

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
			List<PermissionInfo> permissionList = permissionMapper.getPermissions(user.getRoleId());
			data = new UserInfo();
			data.setId(user.getId());
			data.setPassword(user.getPassword());
			data.setName(user.getName());
			data.setNickname(user.getNickname());
			data.setEmail(user.getEmail());
			data.setPhone(user.getPhone());
			data.setRoleId(user.getRoleId());
			data.setRoleName(role.getName());
			data.setRoleLevel(role.getLevel());
			data.setRoleDescription(role.getDescription());
			data.setPermissionList(permissionList);
			userIdCache.save(userId, data);
			return Result.success(data);
		} catch (Exception e) {
			log.error("[getAllRoles]", e);
			return Result.errorService();
		}
	}

	@Override
	public Result addRole() {
		return Result.errorService();
	}

	@Check
	@Override
	public Result getPermissions(PageQuery param) {
		try {
			return Result.page(PageHelper.startPage(PageQuery.resonable(param)).doSelectPage(() -> permissionMapper
				.selectAll()));
		} catch (Exception e) {
			log.error("[getAllRoles]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result getRolePermissions(RolePermissionQuery param) {
		try {
			return Result.page(PageHelper.startPage(PageQuery.resonable(param.getPage())).doSelectPage(
				() -> permissionMapper.getPermissions(param.getRoleId())));
		} catch (Exception e) {
			log.error("[getAllPermissions]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result getRoles(PageQuery param) {
		try {
			return Result.page(PageHelper.startPage(PageQuery.resonable(param)).doSelectPage(() -> roleMapper
				.selectAll()));
		} catch (Exception e) {
			log.error("[getAllRoles]", e);
			return Result.errorService();
		}
	}

}
