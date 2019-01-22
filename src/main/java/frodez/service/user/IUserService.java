package frodez.service.user;

import frodez.config.aop.validation.annotation.Check;
import frodez.dao.param.user.LoginDTO;
import frodez.util.result.Result;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户信息服务
 * @author Frodez
 * @date 2018-11-14
 */
public interface IUserService {

	/**
	 * 获取用户授权信息
	 * @author Frodez
	 * @param userName 用户姓名(唯一)
	 * @date 2018-11-14
	 */
	@Check
	Result getUserInfoByName(@NotBlank(message = "用户名不能为空!") String userName);

	/**
	 * 获取所有权限信息
	 * @author Frodez
	 * @date 2018-12-04
	 */
	Result getAllPermissions();

	/**
	 * 用户登录
	 * @author Frodez
	 * @param LoginDTO 用户登录请求参数
	 * @date 2018-12-03
	 */
	@Check
	Result login(@NotNull(message = "请求参数不能为空!") LoginDTO param);

}
