package info.frodez.service.user;

import info.frodez.dao.param.user.LoginDTO;
import info.frodez.util.result.Result;

/**
 * 用户授权服务
 * @author Frodez
 * @date 2018-11-14
 */
public interface IUserAuthorityService {

	/**
	 * 获取用户授权信息
	 * @author Frodez
	 * @param userName 用户姓名(唯一)
	 * @date 2018-11-14
	 */
	Result getUserInfoByName(String userName);

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
	Result login(LoginDTO param);

}
