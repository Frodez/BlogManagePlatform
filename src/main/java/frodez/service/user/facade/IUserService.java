package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.dao.param.user.RegisterUser;
import frodez.dao.param.user.UpdatePassword;
import frodez.dao.param.user.UpdateUser;
import frodez.util.beans.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户信息服务<br>
 * 用途:<br>
 * 1.注册,注销用户<br>
 * 2.改变密码<br>
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
	@Transactional
	Result register(@Valid @NotNull RegisterUser param);

	/**
	 * 用户注销
	 * @author Frodez
	 * @date 2019-03-15
	 */
	@Transactional
	Result logOff(@NotBlank @Length(min = 3, max = 25) String name, @NotBlank @Length(min = 8, max = 30) String password);

	/**
	 * 更新用户信息
	 * @author Frodez
	 * @date 2019-12-29
	 */
	@Check
	@Transactional
	Result updateUser(@Valid @NotNull UpdateUser param);

	/**
	 * 修改密码
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Check
	@Transactional
	Result updatePassword(@Valid @NotNull UpdatePassword param);

}
