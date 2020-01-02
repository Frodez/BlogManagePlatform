package frodez.service.user.impl;

import frodez.dao.mapper.config.RoleSettingMapper;
import frodez.dao.mapper.permission.MenuMapper;
import frodez.dao.mapper.permission.RoleMenuMapper;
import frodez.dao.mapper.permission.RoleTagMapper;
import frodez.dao.mapper.permission.TagMapper;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.table.permission.RoleMenu;
import frodez.dao.model.table.permission.RoleTag;
import frodez.dao.model.table.user.Role;
import frodez.dao.param.user.CreateRole;
import frodez.dao.param.user.UpdateRole;
import frodez.service.cache.facade.config.SettingCache;
import frodez.service.user.facade.IRoleService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.common.EmptyUtil;
import frodez.util.reflect.BeanUtil;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RoleService implements IRoleService {

	@Autowired
	@Qualifier("settingMapCache")
	private SettingCache settingCache;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private TagMapper tagMapper;

	@Autowired
	private RoleSettingMapper roleSettingMapper;

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	@Autowired
	private RoleTagMapper roleTagMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public Result getRoles(QueryPage query) {
		return query.start(() -> roleMapper.selectAll());
	}

	@Override
	public Result createRole(CreateRole param) {
		List<Long> menuIds = param.getMenuPermissions();
		List<Long> tagIds = param.getTagPermissions();
		if (EmptyUtil.no(menuIds)) {
			if (!menuMapper.existByIds(menuIds)) {
				return Result.fail("存在非法的菜单权限ID");
			}
		}
		if (EmptyUtil.no(tagIds)) {
			if (!tagMapper.existByIds(tagIds)) {
				return Result.fail("存在非法的标签权限ID");
			}
		}
		Role role = BeanUtil.copy(param, Role::new);
		role.setCreateTime(new Date());
		roleMapper.insertUseGeneratedKeys(role);
		Long roleId = role.getId();
		if (EmptyUtil.no(menuIds)) {
			List<RoleMenu> roleMenus = menuIds.stream().map((item) -> {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setMenuId(item);
				roleMenu.setRoleId(roleId);
				return roleMenu;
			}).collect(Collectors.toList());
			roleMenuMapper.insertList(roleMenus);
		}
		if (EmptyUtil.no(tagIds)) {
			List<RoleTag> roleTags = tagIds.stream().map((item) -> {
				RoleTag roleTag = new RoleTag();
				roleTag.setTagId(item);
				roleTag.setRoleId(roleId);
				return roleTag;
			}).collect(Collectors.toList());
			roleTagMapper.insertList(roleTags);
		}
		return Result.success();
	}

	@Override
	public Result updateRole(UpdateRole param) {
		if (!roleMapper.existsWithPrimaryKey(param.getId())) {
			return Result.fail("该角色不存在!");
		}
		roleMapper.updateByPrimaryKeySelective(BeanUtil.copy(param, Role::new));
		return Result.success();
	}

	@Override
	public Result deleteRole(Long roleId) {
		List<Long> userIds = userMapper.partialEqual("id", "role_id", roleId);
		if (EmptyUtil.no(userIds)) {
			return Result.fail("该角色下还有用户存在,请先将属于该角色的用户转移角色");
		}
		//删除角色
		roleMapper.deleteByPrimaryKey(roleId);
		//删除角色对应的菜单权限,标签权限,设置
		roleMenuMapper.deleteEqual("role_id", roleId);
		roleTagMapper.deleteEqual("role_id", roleId);
		roleSettingMapper.deleteEqual("role_id", roleId);
		//更新设置缓存
		settingCache.clear();
		return Result.success();
	}

}
