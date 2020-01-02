package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.constant.enums.user.UserStatus;
import frodez.dao.param.user.RegisterUser;
import frodez.dao.param.user.UpdateUser;
import frodez.util.beans.result.Result;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户信息服务<br>
 * 用途:<br>
 * 1.注册,注销用户<br>
 * 2.禁用和启用用户<br>
 * 3.改变密码<br>
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
	Result logOff();

	/**
	 * 禁用或启用用户
	 * @author Frodez
	 * @date 2019-12-30
	 */
	@Check
	@Transactional
	Result setStatus(@NotNull Long userId, @MapEnum(UserStatus.class) Byte status);

	/**
	 * 批量禁用或启用用户
	 * @author Frodez
	 * @date 2019-12-30
	 */
	@Check
	@Transactional
	Result setStatus(@NotEmpty List<Long> userIds, @MapEnum(UserStatus.class) Byte status);

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
	Result updatePassword(@NotEmpty @Length(max = 2000) String password);

}
