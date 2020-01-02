package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.constant.annotations.decoration.Page;
import frodez.constant.annotations.decoration.ServiceOnly;
import frodez.dao.model.result.user.UserBaseInfo;
import frodez.dao.model.result.user.UserDetail;
import frodez.dao.model.result.user.UserEndpointDetail;
import frodez.dao.model.result.user.UserInfo;
import frodez.dao.param.user.UpdateUserRole;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户信息服务<br>
 * 用途:<br>
 * 1.查看用户信息列表<br>
 * 2.获取用户基本信息,更新用户部分基本信息<br>
 * 3.查看用户详细信息<br>
 * 4.查看,分配用户角色<br>
 * 5.获取用户角色对应的权限<br>
 * @author Frodez
 * @date 2018-11-14
 */
public interface IUserManageService {

	/**
	 * 分页查询用户信息
	 * @author Frodez
	 * @date 2019-12-30
	 */
	@Page
	@Check
	@Success(value = UserBaseInfo.class, containerType = Container.PAGE)
	Result getUsers(@Valid @NotNull QueryPage query);

	/**
	 * 通过用户ID获取用户信息
	 * @author Frodez
	 * @date 2019-12-29
	 */
	@Check
	@Success(UserInfo.class)
	Result getUserInfo(@NotNull Long userId);

	/**
	 * 通过用户名称获取用户信息
	 * @author Frodez
	 * @date 2019-12-29
	 */
	@Check
	@Success(UserInfo.class)
	Result getUserInfo(@NotBlank String userName);

	/**
	 * 通过用户ID获取用户详细信息
	 * @author Frodez
	 * @date 2019-12-27
	 */
	@Check
	@Success(UserDetail.class)
	Result getUserDetail(@NotNull Long userId);

	/**
	 * 通过用户名称获取用户详细信息
	 * @author Frodez
	 * @date 2019-12-27
	 */
	@Check
	@Success(UserDetail.class)
	Result getUserDetail(@NotBlank String userName);

	/**
	 * 通过用户ID获取用户后端端点权限的详细信息
	 * @author Frodez
	 * @date 2019-12-28
	 */
	@Check
	@ServiceOnly
	@Success(UserEndpointDetail.class)
	Result getEndpointPermission(@NotNull Long userId);

	/**
	 * 通过用户名称获取用户后端端点权限的详细信息
	 * @author Frodez
	 * @date 2019-12-28
	 */
	@Check
	@ServiceOnly
	@Success(UserEndpointDetail.class)
	Result getEndpointPermission(@NotBlank String userName);

	/**
	 * 为用户批量赋予新角色信息，原角色信息将会被替代
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Check
	@Transactional
	Result setRole(@Valid @NotNull UpdateUserRole param);

	/**
	 * 为属于某种角色的用户赋予新角色信息，原角色信息将会被替代
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Check
	@Transactional
	Result replaceRole(@NotNull Long former, @NotNull Long latter);

	/**
	 * 把某个角色下的所有用户踢下线
	 * @author Frodez
	 * @date 2020-01-02
	 */
	@Check
	@ServiceOnly
	Result kickAllOut(@NotNull Long roleId);

}
