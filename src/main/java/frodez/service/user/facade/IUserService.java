package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.dao.param.user.Doregister;
import frodez.util.beans.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 用户信息服务
 * @author Frodez
 * @date 2018-11-14
 */
public interface IUserService {

	/**
	 * 用户注册
	 * @author Frodez
	 * @date 2019-02-02
	 */
	@Check
	Result register(@Valid @NotNull Doregister param);

	/**
	 * 用户注销
	 * @author Frodez
	 * @date 2019-03-15
	 */
	Result logOff();

}
