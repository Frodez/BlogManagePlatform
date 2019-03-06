package frodez.service.user.facade;

import frodez.config.aop.validation.annotation.common.NotNullParam;
import frodez.util.beans.result.Result;

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
	Result getUserInfo(@NotNullParam String userName);

	/**
	 * 获取所有权限信息
	 * @author Frodez
	 * @date 2018-12-04
	 */
	Result getAllPermissions();

}
