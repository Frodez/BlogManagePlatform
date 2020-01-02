package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.constant.annotations.decoration.Page;
import frodez.dao.model.table.user.Role;
import frodez.dao.param.user.CreateRole;
import frodez.dao.param.user.UpdateRole;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色信息服务<br>
 * 用途:<br>
 * 1.查询角色列表<br>
 * 2.新建更改角色<br>
 * 3.删除角色<br>
 * @author Frodez
 * @date 2019-12-31
 */
public interface IRoleService {

	/**
	 * 分页查询角色信息
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Page
	@Check
	@Success(value = Role.class, containerType = Container.PAGE)
	Result getRoles(@Valid @NotNull QueryPage query);

	/**
	 * 创建新角色
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Check
	@Transactional
	Result createRole(@Valid @NotNull CreateRole param);

	/**
	 * 更新角色
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Check
	@Transactional
	Result updateRole(@Valid @NotNull UpdateRole param);

	/**
	 * 删除角色
	 * @author Frodez
	 * @date 2020-01-01
	 */
	@Check
	@Transactional
	Result deleteRole(@NotNull Long roleId);

}
