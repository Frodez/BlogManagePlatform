package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.swagger.annotation.Success;
import frodez.dao.model.result.login.RefreshInfo;
import frodez.dao.param.login.LoginUser;
import frodez.dao.param.login.RefreshToken;
import frodez.util.beans.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 登录管理服务<br>
 * 用途:<br>
 * 1.登录,重新登录,退出<br>
 * @author Frodez
 * @date 2018-11-14
 */
public interface ILoginService {

	/**
	 * 用户登录
	 * @author Frodez
	 * @date 2018-12-03
	 */
	@Check
	@Success(String.class)
	Result login(@Valid @NotNull LoginUser param);

	/**
	 * 用户重新登录
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Check
	@Success(RefreshInfo.class)
	Result refresh(@Valid @NotNull RefreshToken param);

	/**
	 * 用户登出
	 * @author Frodez
	 * @date 2019-02-19
	 */
	Result logout();

}
