package frodez.service.user.impl;

import frodez.cache.vm.facade.NameCache;
import frodez.constant.user.UserStatusEnum;
import frodez.service.user.facade.IAuthorityService;
import frodez.service.user.mapper.PermissionMapper;
import frodez.service.user.mapper.RoleMapper;
import frodez.service.user.mapper.UserMapper;
import frodez.service.user.model.Role;
import frodez.service.user.model.User;
import frodez.service.user.result.PermissionInfo;
import frodez.service.user.result.UserInfo;
import frodez.util.result.Result;
import frodez.util.result.ResultUtil;
import java.util.List;
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
		return ResultUtil.errorService();
	}

	@Override
	public Result getUserInfo(String userName) {
		try {
			UserInfo data = nameCache.get(userName);
			if (data != null) {
				return ResultUtil.success(data);
			}
			Example example = new Example(User.class);
			example.createCriteria().andEqualTo("name", userName);
			User user = userMapper.selectOneByExample(example);
			if (user == null) {
				return ResultUtil.fail("未查询到用户信息!");
			}
			if (user.getStatus().equals(UserStatusEnum.FORBIDDEN.getVal())) {
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
			nameCache.save(userName, data);
			return ResultUtil.success(data);
		} catch (Exception e) {
			log.error("[getUserAuthority]", e);
			return ResultUtil.errorService();
		}
	}

	@Override
	public Result getAllPermissions() {
		try {
			return ResultUtil.success(permissionMapper.selectAll());
		} catch (Exception e) {
			log.error("[getAllPermissions]", e);
			return ResultUtil.errorService();
		}
	}

}
