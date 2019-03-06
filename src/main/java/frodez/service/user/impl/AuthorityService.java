package frodez.service.user.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import frodez.config.aop.validation.annotation.common.Check;
import frodez.constant.user.UserStatusEnum;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.user.Role;
import frodez.dao.model.user.User;
import frodez.dao.result.user.PermissionInfo;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.vm.facade.NameCache;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.param.PageDTO;
import frodez.util.beans.result.Result;
import java.util.List;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	private NameCache nameCache;

	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public Result addRole() {
		return Result.errorService();
	}

	@Check
	@Override
	public Result getUserInfo(String userName) {
		try {
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
			nameCache.save(userName, data);
			return Result.success(data);
		} catch (Exception e) {
			log.error("[getUserAuthority]", e);
			return Result.errorService();
		}
	}

	@Override
	public Result getAllPermissions() {
		try {
			return Result.success(permissionMapper.selectAll());
		} catch (Exception e) {
			log.error("[getAllPermissions]", e);
			return Result.errorService();
		}
	}

	/**
	 * 获取所有角色信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@Override
	public Result getAllRoles() {
		try {
			return Result.success(roleMapper.selectAll());
		} catch (Exception e) {
			log.error("[getAllRoles]", e);
			return Result.errorService();
		}
	}

	/**
	 * 根据角色ID获取对应权限信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@Override
	public Result getRolePermissions(Long roleId, @Nullable PageDTO page) {
		try {
			Page<PermissionInfo> info = PageHelper.startPage(PageDTO.resonable(page)).doSelectPage(
				() -> permissionMapper.getPermissions(roleId));
			return Result.page(info.getTotal(), info.getResult());
		} catch (Exception e) {
			log.error("[getAllPermissions]", e);
			return Result.errorService();
		}
	}

}
