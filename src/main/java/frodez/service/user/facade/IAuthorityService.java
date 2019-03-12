package frodez.service.user.facade;

import frodez.util.beans.result.Result;
import javax.validation.constraints.NotBlank;

/**
 * 权限信息服务
 * @author Frodez
 * @date 2018-11-14
 */
public interface IAuthorityService {

	/**
	 * 根据用户名获取用户授权信息
	 * @author Frodez
	 * @date 2018-11-14
	 */
	Result getUserInfo(@NotBlank String userName);

	/**
	 * 获取所有权限信息
	 * @author Frodez
	 * @date 2018-12-04
	 */
	Result getAllPermissions();

}
