package frodez.service.permission.impl;

import frodez.config.aop.exception.annotation.Error;
import frodez.constant.enums.common.ModifyType;
import frodez.constant.enums.permission.PermissionType;
import frodez.constant.errors.code.ErrorCode;
import frodez.dao.mapper.permission.EndpointMapper;
import frodez.dao.mapper.permission.MenuMapper;
import frodez.dao.mapper.permission.RoleMenuMapper;
import frodez.dao.mapper.permission.RoleTagMapper;
import frodez.dao.mapper.permission.TagMapper;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.model.result.permission.MenuPermissionDetail;
import frodez.dao.model.result.permission.PermissionDetail;
import frodez.dao.model.result.permission.TagPermissionDetail;
import frodez.dao.model.table.permission.Endpoint;
import frodez.dao.model.table.permission.Menu;
import frodez.dao.model.table.permission.RoleMenu;
import frodez.dao.model.table.permission.RoleTag;
import frodez.dao.model.table.permission.Tag;
import frodez.dao.param.permission.UpdateRolePermission;
import frodez.service.cache.facade.permission.IPermissionCache;
import frodez.service.permission.facade.IPermissionService;
import frodez.service.user.facade.IUserManageService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.common.EmptyUtil;
import frodez.util.common.StreamUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Error(ErrorCode.AUTHORITY_SERVICE_ERROR)
public class PermissionService implements IPermissionService {

	@Autowired
	@Qualifier("permissionRedisCache")
	private IPermissionCache permissionCache;

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private TagMapper tagMapper;

	@Autowired
	private EndpointMapper endpointMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	@Autowired
	private RoleTagMapper roleTagMapper;

	@Autowired
	private IUserManageService userManageService;

	@Override
	public Result getMenuPermissions(QueryPage query) {
		return query.start(() -> menuMapper.selectAll());
	}

	@Override
	public Result getTagPermissions(QueryPage query) {
		return query.start(() -> tagMapper.selectAll());
	}

	@Override
	public Result getEndpoints(Long roleId) {
		List<Endpoint> endpoints = endpointMapper.getMenuEndpoints(roleId);
		List<Endpoint> tagEndpoints = endpointMapper.getTagEndpoints(roleId);
		endpoints.addAll(tagEndpoints);
		return Result.success(endpoints);
	}

	@Override
	public Result getPermission(Long roleId) {
		//用户权限详情
		PermissionDetail permissionDetail = permissionCache.get(roleId);
		if (permissionDetail != null) {
			return Result.success(permissionDetail);
		}
		permissionDetail = new PermissionDetail();
		//菜单权限详细信息
		List<Menu> menus = menuMapper.getByRoleId(roleId);
		if (EmptyUtil.no(menus)) {
			Map<Long, List<Endpoint>> menuEndpoints = endpointMapper.batchGetByMenuId(StreamUtil.list(menus, Menu::getId));
			List<MenuPermissionDetail> menuPermissions = menus.stream().map((item) -> {
				MenuPermissionDetail menuPermission = new MenuPermissionDetail();
				menuPermission.setMenu(item);
				menuPermission.setEndpoints(menuEndpoints.get(item.getId()));
				return menuPermission;
			}).collect(Collectors.toList());
			permissionDetail.setMenuPermissions(menuPermissions);
		}
		//标签权限详细信息
		List<Tag> tags = tagMapper.getByRoleId(roleId);
		if (EmptyUtil.no(menus)) {
			Map<Long, List<Endpoint>> tagEndpoints = endpointMapper.batchGetByTagId(StreamUtil.list(tags, Tag::getId));
			List<TagPermissionDetail> tagPermissions = tags.stream().map((item) -> {
				TagPermissionDetail tagPermission = new TagPermissionDetail();
				tagPermission.setTag(item);
				tagPermission.setEndpoints(tagEndpoints.get(item.getId()));
				return tagPermission;
			}).collect(Collectors.toList());
			permissionDetail.setTagPermissions(tagPermissions);
		}
		permissionCache.save(roleId, permissionDetail);
		return Result.success(permissionDetail);
	}

	@Override
	public Result updateRolePermission(UpdateRolePermission param) {
		Long roleId = param.getRoleId();
		if (!roleMapper.existsWithPrimaryKey(roleId)) {
			return Result.fail("未找到该角色");
		}
		List<Long> permissionIds = param.getPermissionIds();
		switch (PermissionType.of(param.getPermissionType())) {
			case MENU : {
				switch (ModifyType.of(param.getModifyType())) {
					case INSERT : {
						if (EmptyUtil.no(permissionIds) || !menuMapper.existByIds(permissionIds)) {
							return Result.fail("不合法的菜单权限ID");
						}
						if (EmptyUtil.no(permissionIds, roleMenuMapper.partialEqual("menu_id", "role_id", roleId))) {
							return Result.fail("该角色已经拥有的权限和要添加的菜单权限存在重复!");
						}
						List<RoleMenu> roleMenus = permissionIds.stream().map((item) -> {
							RoleMenu roleMenu = new RoleMenu();
							roleMenu.setRoleId(roleId);
							roleMenu.setMenuId(item);
							return roleMenu;
						}).collect(Collectors.toList());
						roleMenuMapper.insertList(roleMenus);
						break;
					}
					case UPDATE : {
						if (EmptyUtil.no(permissionIds)) {
							List<RoleMenu> roleMenus = permissionIds.stream().map((item) -> {
								RoleMenu roleMenu = new RoleMenu();
								roleMenu.setRoleId(roleId);
								roleMenu.setMenuId(item);
								return roleMenu;
							}).collect(Collectors.toList());
							roleMenuMapper.insertList(roleMenus);
						}
						roleMenuMapper.deleteEqual("role_id", roleId);
						break;
					}
					case DELETE : {
						if (EmptyUtil.no(permissionIds) || !menuMapper.existByIds(permissionIds)) {
							return Result.fail("不合法的菜单权限ID");
						}
						if (!roleMenuMapper.<Long>partialEqual("menu_id", "role_id", roleId).containsAll(permissionIds)) {
							return Result.fail("需要删除的菜单权限不在角色享有权限之内!");
						}
						roleMenuMapper.deleteEqual("role_id", roleId);
						break;
					}
				}
			}
			case TAG : {
				switch (ModifyType.of(param.getModifyType())) {
					case INSERT : {
						if (EmptyUtil.no(permissionIds) || !tagMapper.existByIds(permissionIds)) {
							return Result.fail("不合法的标签权限ID");
						}
						if (!Collections.disjoint(permissionIds, roleTagMapper.<Long>partialEqual("tag_id", "role_id", roleId))) {
							return Result.fail("该角色已经拥有的权限和要添加的标签权限存在重复!");
						}
						List<RoleTag> roleTags = permissionIds.stream().map((item) -> {
							RoleTag roleTag = new RoleTag();
							roleTag.setRoleId(roleId);
							roleTag.setTagId(item);
							return roleTag;
						}).collect(Collectors.toList());
						roleTagMapper.insertList(roleTags);
						break;
					}
					case UPDATE : {
						if (EmptyUtil.no(permissionIds)) {
							List<RoleTag> roleTags = permissionIds.stream().map((item) -> {
								RoleTag roleTag = new RoleTag();
								roleTag.setRoleId(roleId);
								roleTag.setTagId(item);
								return roleTag;
							}).collect(Collectors.toList());
							roleTagMapper.insertList(roleTags);
						}
						roleTagMapper.deleteEqual("role_id", roleId);
						break;
					}
					case DELETE : {
						if (EmptyUtil.no(permissionIds) || !tagMapper.existByIds(permissionIds)) {
							return Result.fail("不合法的标签权限ID");
						}
						if (!roleTagMapper.<Long>partialEqual("tag_id", "role_id", roleId).containsAll(permissionIds)) {
							return Result.fail("需要删除的菜单权限不在角色享有权限之内!");
						}
						roleTagMapper.deleteEqual("role_id", roleId);
						break;
					}
				}
			}
		}
		PermissionDetail detail = getPermission(param.getRoleId()).orThrowMessage().as(PermissionDetail.class);
		permissionCache.save(roleId, detail);
		userManageService.kickAllOut(roleId);
		return Result.success();
	}

}
