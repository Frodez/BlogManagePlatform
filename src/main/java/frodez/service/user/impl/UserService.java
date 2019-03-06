package frodez.service.user.impl;

import com.github.pagehelper.PageHelper;
import frodez.config.aop.validation.annotation.common.Check;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.param.user.RolePermissionDTO;
import frodez.service.user.facade.IUserService;
import frodez.util.beans.param.PageDTO;
import frodez.util.beans.result.Result;
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

	@Override
	public Result addRole() {
		return Result.errorService();
	}

	@Check
	@Override
	public Result getPermissions(PageDTO param) {
		try {
			return Result.page(PageHelper.startPage(PageDTO.resonable(param)).doSelectPage(() -> permissionMapper
				.selectAll()));
		} catch (Exception e) {
			log.error("[getAllRoles]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result getRolePermissions(RolePermissionDTO param) {
		try {
			return Result.page(PageHelper.startPage(PageDTO.resonable(param.getPage())).doSelectPage(
				() -> permissionMapper.getPermissions(param.getRoleId())));
		} catch (Exception e) {
			log.error("[getAllPermissions]", e);
			return Result.errorService();
		}
	}

	@Check
	@Override
	public Result getRoles(PageDTO param) {
		try {
			return Result.page(PageHelper.startPage(PageDTO.resonable(param)).doSelectPage(() -> roleMapper
				.selectAll()));
		} catch (Exception e) {
			log.error("[getAllRoles]", e);
			return Result.errorService();
		}
	}

}
