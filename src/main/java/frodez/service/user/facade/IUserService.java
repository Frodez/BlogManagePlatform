package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.dao.param.user.LoginDTO;
import frodez.dao.param.user.ReLoginDTO;
import frodez.dao.param.user.RegisterDTO;
import frodez.util.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 用户信息服务
 * @author Frodez
 * @date 2018-11-14
 */
public interface IUserService {

	/**
	 * 用户登录
	 * @author Frodez
	 * @date 2018-12-03
	 */
	@Check
	Result login(@Valid @NotNull(message = "请求参数不能为空!") LoginDTO param);

	/**
	 * 用户重新登录
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Check
	Result refresh(@Valid @NotNull(message = "请求参数不能为空!") ReLoginDTO param);

	/**
	 * 用户登出
	 * @author Frodez
	 * @date 2019-02-19
	 */
	Result logout();

	/**
	 * 用户注册
	 * @author Frodez
	 * @date 2019-02-02
	 */
	@Check
	Result register(@Valid @NotNull(message = "请求参数不能为空!") RegisterDTO param);

}
