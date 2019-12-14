package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.dao.param.user.DoLogin;
import frodez.dao.param.user.DoRefresh;
import frodez.util.beans.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 登录管理服务
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
	Result login(@Valid @NotNull DoLogin param);

	/**
	 * 用户重新登录
	 * @author Frodez
	 * @date 2019-02-27
	 */
	@Check
	Result refresh(@Valid @NotNull DoRefresh param);

	/**
	 * 用户登出
	 * @author Frodez
	 * @date 2019-02-19
	 */
	Result logout();

}
