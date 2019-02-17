package frodez.service.user.impl;

import frodez.constant.cache.UserKey;
import frodez.constant.user.UserStatusEnum;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.user.Role;
import frodez.dao.model.user.User;
import frodez.dao.result.user.PermissionInfo;
import frodez.dao.result.user.UserInfo;
import frodez.service.cache.RedisService;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.json.JSONUtil;
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

	/**
	 * redis服务
	 */
	@Autowired
	private RedisService redisService;

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
			redisService.set(UserKey.BASE_INFO + userName, JSONUtil.toJSONString(data));
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
