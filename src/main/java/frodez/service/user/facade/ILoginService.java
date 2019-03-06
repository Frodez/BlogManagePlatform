package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.common.NotNullParam;
import frodez.dao.param.user.LoginParam;
import frodez.dao.param.user.RefreshParam;
import frodez.dao.param.user.RegisterParam;
import frodez.util.beans.result.Result;
import javax.validation.Valid;

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
	Result login(@Valid @NotNullParam LoginParam param);

	/**
	 * 用户重新登录
	 * @author Frodez
	 * @date 2019-02-27
	 */
	Result refresh(@Valid @NotNullParam RefreshParam param);

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
	Result register(@Valid @NotNullParam RegisterParam param);

}
